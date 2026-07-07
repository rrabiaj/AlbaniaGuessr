# AlbaniaGuessr 🏔️🇦🇱

Një lojë ditore gjeografie & historie 100% e fokusuar në Shqipëri.

## Frontend (React)
- **URL**: https://99c7a92d4127352b8180233aa095a002.ctonew.app
- **Stack**: Vite + React 19 + Leaflet + Tailwind CSS
- **Dir**: `/frontend`

### Run:
```bash
cd frontend
npm install
npm run dev     # Dev server on :3000
npm run build   # Production build
```

## Backend (Spring Boot)
- **Stack**: Spring Boot 3.x + JPA + PostgreSQL + Redis
- **Dir**: `/backend`

### Run (Docker):
```bash
cd backend
docker-compose up -d
```

### Run (local dev):
```bash
cd backend
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### API Endpoints:
| Method | Path | Description |
|--------|------|-------------|
| POST | /api/games/start | Start game → sessionId |
| GET | /api/games/{sessionId}/round/{n} | Get photo (NO coords!) |
| POST | /api/games/{sessionId}/round/{n}/guess | Submit guess → score |
| GET | /api/games/{sessionId}/summary | Full game summary |
| GET | /api/daily-challenge | Today's challenge |
| POST | /api/daily-challenge/submit | Submit daily score |
| GET | /api/leaderboard/global | Global top 100 |
| GET | /api/leaderboard/daily | Daily top scores |
| POST | /api/auth/register | Register |
| POST | /api/auth/login | Login → JWT |
| GET | /api/users/me/stats | User stats |
| POST/PUT/GET | /api/admin/locations | Admin CRUD |

### Scoring:
- **Map score**: 5000 × e^(-distanceKm / 200), max 5000
- **Year score**: max(5000 - |yearDiff| × 100, 0), max 5000
- **Per round**: 0-10,000 pts | **Per game (5 rounds)**: 0-50,000 pts

## 18 Locationet:
🇦🇱 Berat, Gjirokastër, Tiranë, Bunk'Art, Shkodër, Butrint, Llogara, Theth, Sarandë, Korçë, Pogradec, Krujë, Apollonia, Durrës, Rozafa, Syri i Kaltër, Ksamil, Vlorë