export interface Location {
  id: number;
  name: string;
  lat: number;
  lng: number;
  year: number;
  fact: string;
  imageId: number;
}

export const locations: Location[] = [
  { id: 1, name: "Kalaja e Beratit", lat: 40.7058, lng: 19.9522, year: 1960, fact: "Kalaja e Beratit, një nga vendbanimet më të vjetra në Shqipëri", imageId: 1 },
  { id: 2, name: "Kalaja e Gjirokastrës", lat: 40.0756, lng: 20.1389, year: 1985, fact: "Qytet muze, trashëgimi botërore e UNESCO-s", imageId: 2 },
  { id: 3, name: "Sheshi Skënderbej", lat: 41.3275, lng: 19.8187, year: 1990, fact: "Zemra e Tiranës, sheshi më i madh në Shqipëri", imageId: 3 },
  { id: 4, name: "Bunk'Art", lat: 41.3245, lng: 19.8208, year: 2015, fact: "Muze i fshehtë nëntokësor i epokës së komunizmit", imageId: 4 },
  { id: 5, name: "Ura e Mesit", lat: 42.0683, lng: 19.5097, year: 1970, fact: "Ura e vjetër osmane mbi lumin Kir në Shkodër", imageId: 5 },
  { id: 6, name: "Butrint", lat: 39.7450, lng: 20.0203, year: 1925, fact: "Një nga qytetet më të lashta në Mesdhe", imageId: 6 },
  { id: 7, name: "Llogara Pass", lat: 40.1969, lng: 19.5917, year: 2005, fact: "Qafa e Llogarasë, 1027 m mbi nivelin e detit", imageId: 7 },
  { id: 8, name: "Theth", lat: 42.3958, lng: 19.7750, year: 1975, fact: "Fshati i izoluar në Alpet Shqiptare, parajsë natyrale", imageId: 8 },
  { id: 9, name: "Sarandë", lat: 39.8750, lng: 20.0050, year: 2010, fact: "Qyteti bregdetar i njohur për plazhet dhe jetën e natës", imageId: 9 },
  { id: 10, name: "Kryekisha e Korçës", lat: 40.6167, lng: 20.7833, year: 2000, fact: "Katedralja 'Ringjallja e Krishtit', një nga më të mëdhatë në Shqipëri", imageId: 10 },
  { id: 11, name: "Pogradec / Liqeni i Ohrit", lat: 40.9000, lng: 20.6542, year: 1980, fact: "Liqeni më i vjetër në Evropë, me ujë kristal", imageId: 11 },
  { id: 12, name: "Kalaja e Krujës", lat: 41.5075, lng: 19.7936, year: 1955, fact: "Kalaja e Gjergj Kastriotit, heroit tonë kombëtar", imageId: 12 },
  { id: 13, name: "Apollonia", lat: 40.7197, lng: 19.4733, year: 1930, fact: "Qytet i lashtë grek, një nga më të rëndësishmit në Illiri", imageId: 13 },
  { id: 14, name: "Amfiteatri i Durrësit", lat: 41.3111, lng: 19.4450, year: 1965, fact: "Amfiteatri më i madh në Ballkan, shekulli II", imageId: 14 },
  { id: 15, name: "Kalaja e Rozafës", lat: 42.0467, lng: 19.4931, year: 1972, fact: "Kalaja legjendare e Shkodrës me legjendën e Rozafës", imageId: 15 },
  { id: 16, name: "Syri i Kaltër", lat: 39.9239, lng: 20.1928, year: 2018, fact: "Burim uji natyror me ngjyrë blu të thellë", imageId: 16 },
  { id: 17, name: "Ishujt e Ksamilit", lat: 39.7694, lng: 19.9936, year: 2020, fact: "Ishuj në jug, plazhi më i famshëm i Rivierës Shqiptare", imageId: 17 },
  { id: 18, name: "Porti i Vlorës", lat: 40.4600, lng: 19.4900, year: 1995, fact: "Porti i dytë më i madh në Shqipëri, hyrja në jug", imageId: 18 },
];

export function getDailySeed(): number {
  const now = new Date();
  return now.getFullYear() * 10000 + (now.getMonth() + 1) * 100 + now.getDate();
}

function seededShuffle<T>(arr: T[], seed: number): T[] {
  const rng = () => {
    seed = (seed * 16807) % 2147483647;
    return (seed - 1) / 2147483646;
  };
  const result = [...arr];
  for (let i = result.length - 1; i > 0; i--) {
    const j = Math.floor(rng() * (i + 1));
    [result[i], result[j]] = [result[j], result[i]];
  }
  return result;
}

export function getDailyLocations(): Location[] {
  const seed = getDailySeed();
  return seededShuffle(locations, seed).slice(0, 5);
}

export function getRandomLocations(count = 5): Location[] {
  const seed = Date.now();
  return seededShuffle(locations, seed).slice(0, count);
}

export function calcMapScore(guessLat: number, guessLng: number, actualLat: number, actualLng: number): number {
  const R = 6371;
  const dLat = ((actualLat - guessLat) * Math.PI) / 180;
  const dLng = ((actualLng - guessLng) * Math.PI) / 180;
  const a = Math.sin(dLat / 2) ** 2 + Math.cos((guessLat * Math.PI) / 180) * Math.cos((actualLat * Math.PI) / 180) * Math.sin(dLng / 2) ** 2;
  const distance = R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return Math.round(5000 * Math.exp(-distance / 200));
}

export function calcYearScore(guessYear: number, actualYear: number): number {
  return Math.max(0, 5000 - Math.abs(guessYear - actualYear) * 100);
}

export function getTotalScore(scores: { mapScore: number; yearScore: number }[]): number {
  return scores.reduce((sum, s) => sum + s.mapScore + s.yearScore, 0);
}

export function generateShareText(totalScore: number): string {
  return `Mora ${totalScore.toLocaleString()} pikë në AlbaniaGuessr! 🇦🇱 A më kalon dot? Luaj tani!`;
}