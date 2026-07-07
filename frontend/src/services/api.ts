const API = '/api';

async function apiFetch(path: string, options?: RequestInit) {
  try {
    const res = await fetch(`${API}${path}`, {
      headers: { 'Content-Type': 'application/json' },
      ...options,
    });
    if (!res.ok) throw new Error(`API error: ${res.status}`);
    return await res.json();
  } catch (e) {
    console.warn(`API call failed (${path}), using mock:`, e);
    return null;
  }
}

export async function apiStartGame(mode: string) {
  return apiFetch('/games/start', {
    method: 'POST',
    body: JSON.stringify({ mode }),
  });
}

export async function apiGetRound(sessionId: string, roundNum: number) {
  return apiFetch(`/games/${sessionId}/round/${roundNum}`);
}

export async function apiSubmitGuess(
  sessionId: string,
  roundNum: number,
  lat: number,
  lng: number,
  year: number
) {
  return apiFetch(`/games/${sessionId}/round/${roundNum}/guess`, {
    method: 'POST',
    body: JSON.stringify({ lat, lng, year }),
  });
}

export async function apiGetSummary(sessionId: string) {
  return apiFetch(`/games/${sessionId}/summary`);
}

export async function apiGetDailyChallenge() {
  return apiFetch('/daily-challenge');
}

export async function apiGetLeaderboard(type: string) {
  return apiFetch(`/leaderboard/${type}?limit=10`);
}

export async function apiCheckHealth() {
  try {
    const res = await fetch(`${API}/games/start`, { method: 'POST' });
    return res.ok;
  } catch {
    return false;
  }
}