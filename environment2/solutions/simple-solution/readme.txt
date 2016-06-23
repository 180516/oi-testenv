testowe rozwiazanie:
l¹d/woda: klasyfikuje na podstawie sredniego koloru piksela (jasny - lad, ciemny - woda)
wilgotnosc - odwrotnie proporcjonalna do jasnosci piksela, z wyzszymi wartosciami dla wartosci wejsciowych ze srodka zbioru wartosci (100-180):
	- jesli wartosc jest pozniej 0 -> klasyfikacja jako brak danych dla danego piksela (wyjsciowa wartosc 0)
	- jesli wartosc srednia (v) w granicach 100-180 -> wyjsciowa wartosc = 255 - v / 6
	- dla pozostalych -> wyjsciowa wartosc = 255 - v / 2
1. przyjmuje jako parametry:
	1. sciezke do katalogu z danymi zgodnymi ze specyfikacj¹ opisana w srodowisku testowym
	2. rodzaj analizy (ls - land/sea lub sm - soil moisture)
2. korzysta z plikow konfiguracyjnych:
- z pliku land-sea-boundary.txt, w ktorym, znajduje sie wartosc graniczna sredniego koloru piksela przy klasyfikacji l¹d/woda
3. generuje wynik w postaci jednokanalowych masek i zapisuje w katalogu results jako *.png, gdzie * to numer wejsciowych danych