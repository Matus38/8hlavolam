# 8hlavolam
Two way search solution

Úlohou programu je násť riešenie 8hlavolamu pomocou obojsmerného prehľadávania.
Hlavolam je zložený z 8 očíslovaných políčok a jedného prázdneho miesta. 
Políčka je možné presúvať hore, dole, vľavo alebo vpravo, ale len ak je tým smerom medzera. 
Je vždy daná nejaká východisková a nejaká cieľová pozícia a je potrebné nájsť postupnosť krokov, ktoré vedú z jednej pozície do druhej.

Príkladom môže byť nasledovná začiatočná a koncová pozícia:<br />
Začiatok:&nbsp;&nbsp;&nbsp;&nbsp;Koniec:<br />
1 2 3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1 2 3<br />
4 5 6&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4 6 8<br />
7 8 0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;7 5 0<br />
Im zodpovedajúca postupnosť krokov je: VPRAVO, DOLE, VĽAVO, HORE.

Vstupné údaje sú uložené v súbore input.txt, ktoré sú formátované nasledovne. 
Jednotlivé úlohy, teda začiatočný aj konečný stav, sú od seba oddelené jedným riadkom,
ktorý môže byť prázdny alebo v ňom môže byť napísané napr. vzorové riešenie.

Stav je reprezentovaný nasledovne. Riadky sú medzi sebou oddelené jednoduchými 
zátvorkami „( )“. V týchto zátvorkách sú zapísané hodnoty pre jednotlivé stĺpce oddelené 
medzerami. Napr. stav 3x3, kde prvý riadok obsahuje stĺpce s hodnotami 1, 2, 3, druhý 
riadok obsahuje hodnoty 4, 5, 6 a tretí riadok obsahuje 7, 8, 0 je zapísaný 
ako (1 2 3)(4 5 6)(7 8 0). Hodnota 0 predstavuje prázdne miesto v hlavolame.

Viem, že obojsmerné prehľadávanie by postačilo riešiť sekvenčne, ale pre zaujímavosť 
som implementoval aj paralelnú verziu. Keďže OS môže vždy prerušiť inak jednotlivé
vlákna tak môžme pre jeden problém nájsť rôzne riešenia.

Program dokáže spracovať aj hlavolam iný než je 3x3. Najprv sa načítajú údaje zo súboru, spracujú sa a začne prehľadávanie.
Jedna vetva prehľadáva priestor zo začiatočnej pozície a druhá z konečnej. Každá vetva má svoju tabuľku s už navštívenými uzlami 
aby sa nevytvárali rovnaké uzly stále dookola. Ďalej má každá vetva svoju frontu, kde sa ukladajú uzly, ktoré sa majú 
prehľadať spôsobom, aký sa využíva pri prehľadávaní do šírky, teda nové uzly pribudnú na koniec fronty. 
Obe vlákna zdieľajú spoločnú tabuľku s uzlami ktoré spoločné navštívili. Tu som využil semafor pre vzájomné vylučovanie. 
Ak teda niektoré vlákno navštívi uzol ktorý už navštívilo druhé vlákno, tak program zistí, že sa našla cesta a vypíše ju. 
V inom prípade ak sa generovanie dostane až do maximálnej povolenej hĺbky, program vypíše hlásenie a hľadanie sa ukončí.

Operátory pohybu som naviazal na prázdne políčko (hodnota 0), ak je teda na výstupe operátor dole znamená to že prázdne 
políčko sa má pohnúť smerom dole. Existujú teda len štyri operátori a to dole (v programe D), hore (U), doľava (L) a doprava (R).

Uzol obsahuje stav, odkaz na predchodcu, hĺbku v akej sa nachádza a posledný použitý operátor, teda operátor ktorým sa doňho dostaneme.
