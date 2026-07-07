const API_BASE = window.location.hostname === 'localhost' 
  ? 'http://localhost:8080/api' 
  : '/api';

export async function startGame(mode: 'classic' | 'daily'): Promise<string> {
  const res = await fetch(`${API_BASE}/games/start`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ mode }),
  });
  const data = await res.json();
  return data.sessionId;
}

export async function getRound(sessionId: string, roundNum: number) {
  const res = await fetch(`${API_BASE}/games/${sessionId}/round/${roundNum}`);
  return res.json();
}

export async function submitGuess(
  sessionId: string, 
  roundNum: number, 
  lat: number, 
  lng: number, 
  year: number
) {
  const res = await fetch(`${API_BASE}/games/${sessionId}/round/${roundNum}/guess`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ lat, lng, year }),
  });
  return res.json();
}

export async function getGameSummary(sessionId: string) {
  const res = await fetch(`${API_BASE}/games/${sessionId}/summary`);
  return res.json();
}

export async function getDailyChallenge() {
  const res = await fetch(`${API_BASE}/daily-challenge`);
  return res.json();
}

export async function getLeaderboard(type: 'global' | 'daily') {
  const res = await fetch(`${API_BASE}/leaderboard/${type}`);
  return res.json();
}