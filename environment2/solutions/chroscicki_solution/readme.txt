Testowe 'proste' rozwiązanie - ulepszenie innego prostego rozwiązania, w którym klasyfikacja odbywa się na podstawie koloru piksela. Dodatkowo dodano filtr medianowy.

ląd/woda: klasyfikuje na podstawie średniego koloru piksela (jasny - ląd, ciemny - woda)
wilgotność - odwrotnie proporcjonalna do jasności piksela, z wyższymi wartościami dla wartości wejściowych ze środka zbioru wartości (100-180):
    - jeśli wartość jest poniżej 1 -> klasyfikacja jako brak danych dla danego piksela (wyjściowa wartosc 0)
    - jeśli wartość średnia (v) w granicach 100-180 -> wyjściowa wartość = 255 - v / 6
    - dla pozostałych -> wyjściowa wartosc = 255 - v / 2
1. przyjmuje jako parametry:
    1. do katalogu z danymi zgodnymi ze specyfikacją opisaną w środowisku testowym
    2. rodzaj analizy (ls - land/sea lub sm - soil moisture)
2. korzysta z plików konfiguracyjnych:
- z pliku land-sea-boundary.txt, w którym, znajduje się wartość graniczna średniego koloru piksela przy klasyfikacji ląd/woda
- z pliku median-filter-mask-size.txt, w którym znajduje się długość maski filtru medianowego (liczba nieparzysta).
3. generuje wynik w postaci jednokanałowych masek i zapisuje w katalogu results jako *.png, gdzie * to numer wejściowych danych
