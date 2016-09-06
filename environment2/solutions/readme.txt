testowe rozwiazanie:
klasyfikuje na podstawie sredniego koloru piksela (jasny - lad, ciemny - woda)
1. nie przyjmuje parametrow
2. korzysta z 3 plikow:
- z obrazu zapisanego w katalogu jako input.bsq
- z pliku input-properties.txt, w ktorym w kolejnych liniach znajduja sie: liczba kanalow, szerokosc, wysokosc obrazu wejsciowego
- z pliku solution-params.txt, w ktorym, w pierwszej linii znajduje sie wartosc graniczna sredniego koloru piksela
3. generuje wynik w postaci jednokanalowej maski i zapisuje ja do pliku output.png