# TicketBooking
Mobilalk projct. Téma: online színházjehy vásárlás.

Kedves Javító!

Ebben a file-ban összegyűjtöttem néhány követelmény megvalósításának helyét, amit esetleg kicsit nehezebb lehet megtalálni. (Az itt nem említett követelmények is értelemszerűen meg vannak valósítva.) 

"Legalább 2 különböző animáció használata": 
Ezt megfigyelheted a MainActivityben a Bejelentkezés és Regisztráció gombokon (blinking animation). A másik animációt a SeatBookingActivityben figyelheted meg a gombokon (bouncing animation).

"Legalább egy Lifecycle Hook használata a teljes projektben: 
Itt minden activityben felhasználtam az OnStart-ot. Itt ellenőrizzük, hogy van e megfelelő internetkapcsolat és ha nincs egy popup figyelmezteti a felhasználót erről.

"Legalább egy olyan androidos erőforrás használata, amihez kell android permission":
Ez a permission:
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 A fent említett internet ellenőrzéshez szükséges.

"CRUD műveletek mindegyike megvalósult és műveletek service-(ek)be vannak kiszervezve (AsyncTasks)": Itt a delete műveletet szeretném megemlíteni, aminek a működése talán nem annyira látványos. Az adatbázisból minden alkalommal, amikor elindul a BrowsingActivity, törlésre kerülnek azok az előadások, amiknek az időpontja a mai dátumhoz képest már elmúltak.

"Legalább 2 komplex Firestore lekérdezés megvalósítása, amely indexet igényel (ide tartoznak: where feltétel, rendezés, léptetés, limitálás)": Az egyik ilyen a BrowsingActivityben megvalósított listázó művelet, a másik pedig a NoSeatAvailableActivityben található. Ez abban az esetben, ha minden hely betelt az adott előadásra kilistázza, hogy milyen egyéb időpontokban  érhető még el a darab. 

Kellemes javítást!
