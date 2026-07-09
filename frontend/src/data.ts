export interface Location {
  id: number;
  name: string;
  lat: number;
  lng: number;
  year: number;
  fact: string;
  imageUrl: string;
}

// FOTO REALE nga Wikimedia Commons — çdo vend ka foton e vet unike
export const locations: Location[] = [
  {
    id: 1, name: "Kalaja e Beratit", lat: 40.7058, lng: 19.9522, year: 2008,
    fact: "Kalaja e Beratit, një nga vendbanimet më të vjetra në Shqipëri, me dritare karakteristike të mëdha. Trashëgimi UNESCO që nga viti 2008", 
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/d/d5/Berat_Castle_%28by_Pudelek%29_2.JPG"
  },
  {
    id: 2, name: "Kalaja e Gjirokastrës", lat: 40.0756, lng: 20.1389, year: 2005,
    fact: "Qytet muze, trashëgimi botërore e UNESCO-s, i njohur për arkitekturën e tij unike guri. Në Listen e UNESCO që nga 2005",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/b/b5/Gjirokast%C3%ABr_Castle_%28by_Pudelek%29_3.jpg"
  },
  {
    id: 3, name: "Sheshi Skënderbej", lat: 41.3275, lng: 19.8187, year: 2017,
    fact: "Zemra e Tiranës, sheshi më i madh në Shqipëri me statujën e heroit kombëtar. Rikonstruktuar në vitin 2017",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/4/45/Monumento_a_Skanderbeg%2C_Tirana%2C_Albania%2C_2014-04-17%2C_DD_03.JPG"
  },
  {
    id: 4, name: "Bunk'Art", lat: 41.3245, lng: 19.8208, year: 2014,
    fact: "Muze i fshehtë nëntokësor i epokës së komunizmit, në Tiranë. Hapur për publikun në vitin 2014",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/f/f0/Tirana_BunkArt_ClockTower_%28WPWTR16%29.JPG"
  },
  {
    id: 5, name: "Ura e Mesit", lat: 42.0683, lng: 19.5097, year: 1770,
    fact: "Ura e vjetër osmane mbi lumin Kir në Shkodër, e ndërtuar në shekullin e XVIII nga pashallëku i Bushatllinjve",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/b/b2/Ura_e_Mesit%2C_Shkoder.jpg"
  },
  {
    id: 6, name: "Butrint", lat: 39.7450, lng: 20.0203, year: 1928,
    fact: "Një nga qytetet më të lashta në Mesdhe, trashëgimi botërore e UNESCO-s. Gërmimet e para arkeologjike në 1928",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/b/b7/Butrint_%28Buthrotum%29%2C_Albania_%28by_Pudelek%29_04_Triconch_Palace.jpg"
  },
  {
    id: 7, name: "Qafa e Llogarasë", lat: 40.1969, lng: 19.5917, year: 2010,
    fact: "Qafa e Llogarasë, 1027 m mbi nivelin e detit, me pamje spektakolare mbi bregdetin shqiptar",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/a/a7/Llogara_Viewing_platform_view.JPG"
  },
  {
    id: 8, name: "Theth", lat: 42.3958, lng: 19.7750, year: 2010,
    fact: "Fshati i izoluar në Alpet Shqiptare, pjesë e Parkut Kombëtar të Thethit që nga viti 1966",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/7/76/Kisha_e_Thethit_-_2018_%288%29.jpg"
  },
  {
    id: 9, name: "Sarandë", lat: 39.8750, lng: 20.0050, year: 2005,
    fact: "Qyteti bregdetar i njohur për plazhet dhe jetën e natës në jug të Shqipërisë. Zhvillim i madh turistik pas 2000",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/c/c1/Saranda_Albania_3_-_beachfront_construction.jpg"
  },
  {
    id: 10, name: "Kryekisha e Korçës", lat: 40.6167, lng: 20.7833, year: 2011,
    fact: "Katedralja 'Ringjallja e Krishtit', një nga më të mëdhatë në Shqipëri. Përfundoi në vitin 2011",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/7/7c/Kor%C3%A7%C3%AB%2C_Albania_-_panoramio_%284%29.jpg"
  },
  {
    id: 11, name: "Pogradec / Liqeni i Ohrit", lat: 40.9000, lng: 20.6542, year: 1979,
    fact: "Liqeni më i vjetër në Evropë, me ujë kristal dhe plazhe të mrekullueshme. Trashëgimi UNESCO nga viti 1979",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/c/c0/Ohrid_Lake_in_Albania_-_bunker.JPG"
  },
  {
    id: 12, name: "Kalaja e Krujës", lat: 41.5075, lng: 19.7936, year: 1955,
    fact: "Kalaja e Gjergj Kastriotit Skënderbeut, heroit tonë kombëtar. Muzeu u hap në 1955",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/0/02/Castillo_de_Kruja%2C_Kruja%2C_Albania%2C_2014-04-18%2C_DD_18.JPG"
  },
  {
    id: 13, name: "Apollonia", lat: 40.7197, lng: 19.4733, year: 1930,
    fact: "Qytet i lashtë ilir dhe grek, një nga më të rëndësishmit në Iliri. Gërmimet filluan në vitet 1930",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/3/35/Apollonia%2C_Albania_-_panorama_%28by_Pudelek%29.JPG"
  },
  {
    id: 14, name: "Amfiteatri i Durrësit", lat: 41.3111, lng: 19.4450, year: 1965,
    fact: "Amfiteatri më i madh në Ballkan, shekulli II pas Krishtit. Zbuluar nga gërmimet në vitin 1965",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/a/ad/Amfiteatr_rzymski_w_Durr%C3%ABs_1.jpg"
  },
  {
    id: 15, name: "Kalaja e Rozafës", lat: 42.0467, lng: 19.4931, year: 1970,
    fact: "Kalaja legjendare e Shkodrës me legjendën e Rozafës. Pamje mbi qytetin dhe liqenin e Shkodrës",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/c/c1/Castillo_de_Rozafa%2C_Shkodra%2C_Albania%2C_2014-04-18%2C_DD_11.JPG"
  },
  {
    id: 16, name: "Syri i Kaltër", lat: 39.9239, lng: 20.1928, year: 2015,
    fact: "Burim uji natyror me ngjyrë blu të thellë, 50 m thellësi. Atraksion turistik natyror",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/a/a8/Blue_Eye_%28Syri_i_kalt%C3%ABr%29_03.JPG"
  },
  {
    id: 17, name: "Ishujt e Ksamilit", lat: 39.7694, lng: 19.9936, year: 2020,
    fact: "Ishuj në jug, plazhi më i famshëm i Rivierës Shqiptare. Të mbrojtur si zonë natyrore",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/5/51/Ksamil-ksamil_islands.jpg"
  },
  {
    id: 18, name: "Porti i Vlorës", lat: 40.4600, lng: 19.4900, year: 1995,
    fact: "Porti i dytë më i madh në Shqipëri, hyrja në jug të vendit. I rëndësishëm për tregtinë detare",
    imageUrl: "https://upload.wikimedia.org/wikipedia/commons/4/42/Vlora_harbor_Albania.jpg"
  },
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