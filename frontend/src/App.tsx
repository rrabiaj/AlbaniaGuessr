import { useState, useCallback, useEffect } from "react";
import { MapContainer, TileLayer, useMapEvents, Marker, Polyline, Popup } from "react-leaflet";
import L from "leaflet";
import {
  Location,
  getDailyLocations,
  getRandomLocations,
  calcMapScore,
  calcYearScore,
  generateShareText,
} from "./data";

// Fix Leaflet default icon
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
});

const guessIcon = new L.DivIcon({
  className: "guess-marker",
  html: '<div style="background:#E3001B;width:24px;height:24px;border-radius:50%;border:3px solid white;box-shadow:0 2px 8px rgba(0,0,0,0.5);"></div>',
  iconSize: [24, 24],
  iconAnchor: [12, 12],
});

const correctIcon = new L.DivIcon({
  className: "correct-marker",
  html: '<div style="background:#22c55e;width:24px;height:24px;border-radius:50%;border:3px solid white;box-shadow:0 2px 8px rgba(0,0,0,0.5);"></div>',
  iconSize: [24, 24],
  iconAnchor: [12, 12],
});

type Page = "home" | "play" | "result" | "leaderboard";
type GameMode = "classic" | "daily";

interface RoundScore {
  location: Location;
  guessLat: number;
  guessLng: number;
  guessYear: number;
  mapScore: number;
  yearScore: number;
  totalRound: number;
  distanceKm: number;
  yearDiff: number;
}

interface LeaderboardEntry {
  name: string;
  score: number;
  date: string;
}

// In-memory leaderboard
const globalLeaderboard: LeaderboardEntry[] = [];
const dailyLeaderboard: LeaderboardEntry[] = [];

function ClickHandler({
  onMapClick,
  disabled,
}: {
  onMapClick: (lat: number, lng: number) => void;
  disabled: boolean;
}) {
  useMapEvents({
    click(e) {
      if (!disabled) {
        onMapClick(e.latlng.lat, e.latlng.lng);
      }
    },
  });
  return null;
}

function App() {
  const [page, setPage] = useState<Page>("home");
  const [mode, setMode] = useState<GameMode>("classic");
  const [rounds, setRounds] = useState<Location[]>([]);
  const [currentRound, setCurrentRound] = useState(0);
  const [guessLat, setGuessLat] = useState<number | null>(null);
  const [guessLng, setGuessLng] = useState<number | null>(null);
  const [guessYear, setGuessYear] = useState(1990);
  const [scores, setScores] = useState<RoundScore[]>([]);
  const [showResult, setShowResult] = useState(false);
  const [animatedScore, setAnimatedScore] = useState(0);
  const [playerName, setPlayerName] = useState("");
  const [showNameInput, setShowNameInput] = useState(false);
  const [leaderboardTab, setLeaderboardTab] = useState<"global" | "daily">("global");

  const startGame = useCallback((gameMode: GameMode) => {
    setMode(gameMode);
    const locs = gameMode === "daily" ? getDailyLocations() : getRandomLocations(5);
    setRounds(locs);
    setCurrentRound(0);
    setScores([]);
    setGuessLat(null);
    setGuessLng(null);
    setGuessYear(1990);
    setShowResult(false);
    setAnimatedScore(0);
    setPage("play");
  }, []);

  const handleMapClick = useCallback((lat: number, lng: number) => {
    setGuessLat(lat);
    setGuessLng(lng);
  }, []);

  const handleSubmitGuess = useCallback(() => {
    if (guessLat === null || guessLng === null) return;

    const loc = rounds[currentRound];
    const mapScore = calcMapScore(guessLat, guessLng, loc.lat, loc.lng);
    const yearScore = calcYearScore(guessYear, loc.year);
    const R = 6371;
    const dLat = ((loc.lat - guessLat) * Math.PI) / 180;
    const dLng = ((loc.lng - guessLng) * Math.PI) / 180;
    const a =
      Math.sin(dLat / 2) ** 2 +
      Math.cos((guessLat * Math.PI) / 180) * Math.cos((loc.lat * Math.PI) / 180) * Math.sin(dLng / 2) ** 2;
    const distanceKm = R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    const newScore: RoundScore = {
      location: loc,
      guessLat,
      guessLng,
      guessYear,
      mapScore,
      yearScore,
      totalRound: mapScore + yearScore,
      distanceKm: Math.round(distanceKm * 10) / 10,
      yearDiff: Math.abs(guessYear - loc.year),
    };

    setScores((prev) => [...prev, newScore]);
    setShowResult(true);

    // Animate score
    let count = 0;
    const target = newScore.totalRound;
    const interval = setInterval(() => {
      count += Math.ceil(target / 20);
      if (count >= target) {
        count = target;
        clearInterval(interval);
      }
      setAnimatedScore(count);
    }, 40);
  }, [guessLat, guessLng, guessYear, currentRound, rounds]);

  const nextRound = useCallback(() => {
    if (currentRound < 4) {
      setCurrentRound((r) => r + 1);
      setGuessLat(null);
      setGuessLng(null);
      setGuessYear(1990);
      setShowResult(false);
      setAnimatedScore(0);
      setTimer(30);
      setTimerActive(true);
    } else {
      setShowNameInput(true);
    }
  }, [currentRound]);

  const finishGame = useCallback(() => {
    const total = scores.reduce((s, r) => s + r.totalRound, 0);
    if (playerName.trim()) {
      const entry: LeaderboardEntry = {
        name: playerName.trim(),
        score: total,
        date: new Date().toLocaleDateString("sq-AL"),
      };
      globalLeaderboard.push(entry);
      globalLeaderboard.sort((a, b) => b.score - a.score);
      if (mode === "daily") {
        dailyLeaderboard.push(entry);
        dailyLeaderboard.sort((a, b) => b.score - a.score);
      }
    }
    setShowNameInput(false);
    setPage("result");
  }, [scores, playerName, mode]);

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [page, currentRound, showResult]);

  const resetGame = () => {
    setPage("home");
    setScores([]);
    setCurrentRound(0);
    setShowResult(false);
  };

  const totalScore = scores.reduce((s, r) => s + r.totalRound, 0);

  // HOME PAGE
  if (page === "home") {
    return (
      <div className="min-h-screen bg-gray-950 flex flex-col">
        <header className="flex items-center justify-center gap-3 py-8 border-b border-gray-800">
          <span className="text-4xl">🇦🇱</span>
          <h1 className="text-4xl font-bold tracking-tight text-white">AlbaniaGuessr</h1>
        </header>
        <main className="flex-1 flex flex-col items-center justify-center gap-8 px-4 py-12">
          <div className="text-center max-w-lg">
            <p className="text-gray-400 text-lg mb-2">
              Gjej vendndodhjen dhe vitin e monumenteve shqiptare në 5 raunde!
            </p>
            <p className="text-gray-500 text-sm">
              Kombinon hartën interaktive me hamendësimin e viteve për të
              mbledhur sa më shumë pikë.
            </p>
          </div>
          <div className="flex flex-col sm:flex-row gap-4 w-full max-w-md">
            <button
              onClick={() => startGame("classic")}
              className="flex-1 bg-[#E3001B] hover:bg-[#cc0018] text-white font-bold py-4 px-6 rounded-xl text-lg transition-all hover:scale-105 shadow-lg shadow-red-900/30"
            >
              🎮 Luaj Tani
            </button>
            <button
              onClick={() => startGame("daily")}
              className="flex-1 bg-gray-800 hover:bg-gray-700 text-white font-bold py-4 px-6 rounded-xl text-lg transition-all hover:scale-105 border border-gray-700"
            >
              📅 Sfida Ditore
            </button>
          </div>
          <div className="flex gap-4">
            <button
              onClick={() => setPage("leaderboard")}
              className="text-gray-400 hover:text-white underline underline-offset-4 text-sm transition-colors"
            >
              🏆 Leaderboard
            </button>
            <button
              onClick={() => {
                const text = generateShareText(0);
                navigator.clipboard.writeText(text);
                alert("Teksti u kopjua!");
              }}
              className="text-gray-400 hover:text-white underline underline-offset-4 text-sm transition-colors"
            >
              📤 Ndaj
            </button>
          </div>
        </main>
      </div>
    );
  }

  // PLAY PAGE
  if (page === "play") {
    const loc = rounds[currentRound];

    if (showResult && scores.length > 0) {
      const lastScore = scores[scores.length - 1];
      return (
        <div className="min-h-screen bg-gray-950 flex flex-col">
          <header className="flex items-center justify-between px-4 py-3 border-b border-gray-800">
            <div className="flex items-center gap-2">
              <span className="text-xl">🇦🇱</span>
              <span className="text-white font-bold">AlbaniaGuessr</span>
            </div>
            <span className="text-gray-400 text-sm">
              Raundi {currentRound + 1}/5
            </span>
          </header>

          <main className="flex-1 flex flex-col items-center p-4 gap-4 max-w-2xl mx-auto w-full">
            <div className="animate-fly-in w-full">
              <div className="bg-gray-900 rounded-2xl p-4 border border-gray-800">
                <p className="text-gray-400 text-xs uppercase tracking-wider mb-2">
                  Vendndodhja e saktë
                </p>
                <h2 className="text-white text-xl font-bold">{lastScore.location.name}</h2>

                <div className="grid grid-cols-2 gap-4 mt-4">
                  <div className="bg-gray-950 rounded-xl p-3 text-center">
                    <p className="text-gray-500 text-xs uppercase">Distanca</p>
                    <p className="text-white text-xl font-bold">{lastScore.distanceKm} km</p>
                  </div>
                  <div className="bg-gray-950 rounded-xl p-3 text-center">
                    <p className="text-gray-500 text-xs uppercase">Viti</p>
                    <p className="text-white text-xl font-bold">
                      {lastScore.guessYear} → {lastScore.location.year}
                    </p>
                  </div>
                  <div className="bg-gray-950 rounded-xl p-3 text-center">
                    <p className="text-gray-500 text-xs uppercase">Harta</p>
                    <p className="text-[#E3001B] text-xl font-bold">{lastScore.mapScore}</p>
                  </div>
                  <div className="bg-gray-950 rounded-xl p-3 text-center">
                    <p className="text-gray-500 text-xs uppercase">Viti</p>
                    <p className="text-[#E3001B] text-xl font-bold">{lastScore.yearScore}</p>
                  </div>
                </div>

                <div className="mt-4 text-center">
                  <p className="text-gray-400 text-sm uppercase">Pikët e raundit</p>
                  <p className="text-white text-5xl font-bold animate-score-pop">
                    {animatedScore.toLocaleString()}
                  </p>
                </div>

                <div className="mt-4 bg-gray-950 rounded-xl p-3">
                  <p className="text-gray-400 text-xs uppercase mb-1">💡 Fakt interesant</p>
                  <p className="text-gray-200 text-sm">{lastScore.location.fact}</p>
                </div>
              </div>
            </div>

            {/* Mini map with guess and correct */}
            <div className="w-full h-48 rounded-xl overflow-hidden border border-gray-800">
              <MapContainer
                center={[lastScore.location.lat, lastScore.location.lng]}
                zoom={8}
                className="w-full h-full"
                zoomControl={false}
                scrollWheelZoom={false}
              >
                <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                <Marker position={[lastScore.guessLat, lastScore.guessLng]} icon={guessIcon}>
                  <Popup>Supozimi yt</Popup>
                </Marker>
                <Marker position={[lastScore.location.lat, lastScore.location.lng]} icon={correctIcon}>
                  <Popup>Vendndodhja e saktë</Popup>
                </Marker>
                <Polyline
                  positions={[
                    [lastScore.guessLat, lastScore.guessLng],
                    [lastScore.location.lat, lastScore.location.lng],
                  ]}
                  pathOptions={{ color: "#E3001B", weight: 2, dashArray: "5,5" }}
                />
              </MapContainer>
            </div>

            <button
              onClick={nextRound}
              className="w-full bg-[#E3001B] hover:bg-[#cc0018] text-white font-bold py-4 rounded-xl text-lg transition-all"
            >
              {currentRound < 4 ? "👉 Raundi Tjetër" : "🏆 Shiko Rezultatin"}
            </button>
          </main>
        </div>
      );
    }

    return (
      <div className="min-h-screen bg-gray-950 flex flex-col">
        <header className="flex items-center justify-between px-4 py-3 border-b border-gray-800">
          <div className="flex items-center gap-2">
            <span className="text-xl">🇦🇱</span>
            <span className="text-white font-bold">AlbaniaGuessr</span>
          </div>
          <div className="flex items-center gap-3">
            <div className="text-right">
              <p className="text-gray-400 text-xs">Pikët totale</p>
              <p className="text-white font-bold">{totalScore.toLocaleString()}</p>
            </div>
            {timerActive && (
              <div className={`rounded-full px-3 py-1 text-sm font-bold ${timer <= 10 ? "bg-red-900 text-red-300" : "bg-gray-800 text-white"}`}>
                {timer}s
              </div>
            )}
          </div>
        </header>

        {/* Progress bar */}
        <div className="w-full bg-gray-900 h-1">
          <div
            className="bg-[#E3001B] h-1 transition-all duration-300"
            style={{ width: `${((currentRound + (showResult ? 1 : 0)) / 5) * 100}%` }}
          />
        </div>

        <main className="flex-1 flex flex-col lg:flex-row">
          {/* Left: Image & Year */}
          <div className="lg:w-1/3 p-4 flex flex-col gap-4">
            <div className="bg-gray-900 rounded-2xl overflow-hidden border border-gray-800">
              <div className="relative w-full h-48 sm:h-64 bg-gray-800">
                  <img
                    src={loc.imageUrl}
                    alt={loc.name}
                    className="w-full h-full object-cover"
                    loading="lazy"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-gray-950/80 to-transparent" />
                  <div className="absolute bottom-3 left-3">
                    <p className="text-white text-sm font-bold">Raundi {currentRound + 1}/5</p>
                    <p className="text-gray-300 text-xs">Ku dhe kur është kjo foto?</p>
                  </div>
                </div>
            </div>

            {/* Year slider */}
            <div className="bg-gray-900 rounded-2xl p-4 border border-gray-800">
              <label className="text-gray-400 text-xs uppercase tracking-wider">
                Viti: <span className="text-white font-bold">{guessYear}</span>
              </label>
              <input
                type="range"
                min={1920}
                max={2026}
                value={guessYear}
                onChange={(e) => setGuessYear(Number(e.target.value))}
                className="w-full mt-2 accent-[#E3001B]"
              />
              <div className="flex justify-between text-gray-600 text-xs mt-1">
                <span>1920</span>
                <span>2026</span>
              </div>
            </div>

            {/* Submit button */}
            <button
              onClick={handleSubmitGuess}
              disabled={guessLat === null || guessLng === null}
              className={`w-full font-bold py-4 rounded-xl text-lg transition-all ${
                guessLat !== null && guessLng !== null
                  ? "bg-[#E3001B] hover:bg-[#cc0018] text-white shadow-lg shadow-red-900/30"
                  : "bg-gray-800 text-gray-500 cursor-not-allowed"
              }`}
            >
              {guessLat !== null && guessLng !== null
                ? "✅ Konfirmo Përgjigjen"
                : "👆 Kliko Hartën"}
            </button>
          </div>

          {/* Right: Map */}
          <div className="lg:w-2/3 p-4">
            <div className="w-full h-full min-h-[50vh] rounded-2xl overflow-hidden border border-gray-800">
              <MapContainer
                center={[41.1533, 20.1683]}
                zoom={7}
                className="w-full h-full"
                scrollWheelZoom={true}
              >
                <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                <ClickHandler onMapClick={handleMapClick} disabled={false} />
                {guessLat !== null && guessLng !== null && (
                  <Marker position={[guessLat, guessLng]} icon={guessIcon}>
                    <Popup>Supozimi yt</Popup>
                  </Marker>
                )}
              </MapContainer>
            </div>
          </div>
        </main>
      </div>
    );
  }

  // NAME INPUT (transitional)
  if (showNameInput) {
    return (
      <div className="min-h-screen bg-gray-950 flex items-center justify-center p-4">
        <div className="bg-gray-900 rounded-2xl p-8 max-w-sm w-full border border-gray-800 text-center">
          <span className="text-4xl block mb-4">🏆</span>
          <h2 className="text-white text-2xl font-bold mb-2">Fantastike!</h2>
          <p className="text-gray-400 mb-6">
            Pikët e tua: <span className="text-white font-bold text-xl">{totalScore.toLocaleString()}</span>
          </p>
          <input
            type="text"
            value={playerName}
            onChange={(e) => setPlayerName(e.target.value)}
            placeholder="Emri yt (opsional)"
            className="w-full bg-gray-800 text-white rounded-xl px-4 py-3 mb-4 border border-gray-700 focus:border-[#E3001B] outline-none"
            onKeyDown={(e) => e.key === "Enter" && finishGame()}
          />
          <button
            onClick={finishGame}
            className="w-full bg-[#E3001B] hover:bg-[#cc0018] text-white font-bold py-3 rounded-xl transition-all"
          >
            Shiko Rezultatin
          </button>
        </div>
      </div>
    );
  }

  // RESULTS PAGE
  if (page === "result") {
    return (
      <div className="min-h-screen bg-gray-950 flex flex-col">
        <header className="flex items-center justify-center gap-3 py-6 border-b border-gray-800">
          <span className="text-3xl">🇦🇱</span>
          <h1 className="text-2xl font-bold text-white">AlbaniaGuessr</h1>
        </header>

        <main className="flex-1 p-4 max-w-2xl mx-auto w-full">
          <div className="text-center mb-8">
            <p className="text-gray-400 uppercase text-sm tracking-wider">Rezultati Përfundimtar</p>
            <p className="text-6xl font-bold text-white mt-2">{totalScore.toLocaleString()}</p>
            <p className="text-gray-500 text-sm">
              nga 50,000 pikë të mundshme
            </p>
          </div>

          {/* Round breakdown */}
          <div className="space-y-3 mb-8">
            {scores.map((s, i) => (
              <div
                key={i}
                className="bg-gray-900 rounded-xl p-4 border border-gray-800 animate-fly-in"
                style={{ animationDelay: `${i * 100}ms` }}
              >
                <div className="flex justify-between items-start">
                  <div>
                    <p className="text-white font-bold">
                      Raundi {i + 1}: {s.location.name}
                    </p>
                    <p className="text-gray-500 text-xs">
                      {s.distanceKm} km larg | {s.yearDiff} vite diferencë
                    </p>
                  </div>
                  <p className="text-[#E3001B] font-bold text-xl">{s.totalRound.toLocaleString()}</p>
                </div>
                <div className="flex gap-4 mt-2 text-xs text-gray-400">
                  <span>🗺️ {s.mapScore} pikë</span>
                  <span>📅 {s.yearScore} pikë</span>
                </div>
              </div>
            ))}
          </div>

          {/* Summary map */}
          <div className="w-full h-64 rounded-xl overflow-hidden border border-gray-800 mb-8">
            <MapContainer center={[41.1533, 20.1683]} zoom={7} className="w-full h-full" scrollWheelZoom={false}>
              <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
              {scores.map((s, i) => (
                <div key={i}>
                  <Marker position={[s.guessLat, s.guessLng]} icon={guessIcon}>
                    <Popup>Raundi {i + 1}: Supozimi</Popup>
                  </Marker>
                  <Marker position={[s.location.lat, s.location.lng]} icon={correctIcon}>
                    <Popup>Raundi {i + 1}: {s.location.name}</Popup>
                  </Marker>
                  <Polyline
                    positions={[
                      [s.guessLat, s.guessLng],
                      [s.location.lat, s.location.lng],
                    ]}
                    pathOptions={{ color: "#E3001B", weight: 1.5, opacity: 0.5 }}
                  />
                </div>
              ))}
            </MapContainer>
          </div>

          {/* Share & Replay */}
          <div className="flex flex-col sm:flex-row gap-3">
            <button
              onClick={() => {
                const text = generateShareText(totalScore);
                navigator.clipboard.writeText(text);
                alert("Teksti u kopjua në clipboard! 📋");
              }}
              className="flex-1 bg-gray-800 hover:bg-gray-700 text-white font-bold py-3 rounded-xl transition-all border border-gray-700"
            >
              📤 Ndaj Rezultatin
            </button>
            <button
              onClick={resetGame}
              className="flex-1 bg-[#E3001B] hover:bg-[#cc0018] text-white font-bold py-3 rounded-xl transition-all"
            >
              🔄 Luaj Përsëri
            </button>
          </div>
        </main>
      </div>
    );
  }

  // LEADERBOARD PAGE
  if (page === "leaderboard") {
    const data = leaderboardTab === "global" ? globalLeaderboard : dailyLeaderboard;
    return (
      <div className="min-h-screen bg-gray-950 flex flex-col">
        <header className="flex items-center justify-between px-4 py-3 border-b border-gray-800">
          <button onClick={() => setPage("home")} className="text-gray-400 hover:text-white transition-colors">
            ← Back
          </button>
          <h1 className="text-white font-bold text-lg">🏆 Leaderboard</h1>
          <div className="w-12" />
        </header>

        <div className="flex border-b border-gray-800">
          <button
            onClick={() => setLeaderboardTab("global")}
            className={`flex-1 py-3 text-sm font-bold transition-colors ${
              leaderboardTab === "global"
                ? "text-[#E3001B] border-b-2 border-[#E3001B]"
                : "text-gray-500 hover:text-gray-300"
            }`}
          >
            🌍 Global
          </button>
          <button
            onClick={() => setLeaderboardTab("daily")}
            className={`flex-1 py-3 text-sm font-bold transition-colors ${
              leaderboardTab === "daily"
                ? "text-[#E3001B] border-b-2 border-[#E3001B]"
                : "text-gray-500 hover:text-gray-300"
            }`}
          >
            📅 Ditor
          </button>
        </div>

        <main className="flex-1 p-4 max-w-lg mx-auto w-full">
          {data.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg">Nuk ka rezultate ende</p>
              <p className="text-gray-600 text-sm mt-2">Luaj një lojë për të parë leaderboard-in!</p>
            </div>
          ) : (
            <div className="space-y-2">
              {data.slice(0, 10).map((entry, i) => (
                <div
                  key={i}
                  className="bg-gray-900 rounded-xl p-3 border border-gray-800 flex items-center gap-3"
                >
                  <span className={`text-lg font-bold w-8 ${i < 3 ? "text-yellow-500" : "text-gray-500"}`}>
                    {i + 1}
                  </span>
                  <div className="flex-1">
                    <p className="text-white font-bold">{entry.name}</p>
                    <p className="text-gray-500 text-xs">{entry.date}</p>
                  </div>
                  <p className="text-[#E3001B] font-bold">{entry.score.toLocaleString()}</p>
                </div>
              ))}
            </div>
          )}
        </main>
      </div>
    );
  }

  return null;
}

export default App;