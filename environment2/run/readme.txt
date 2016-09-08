uruchamianie:
./run.sh katalog typ > output.txt

typ to rodzaj przeprowadzanej analizy:
ls (land/sea) - klasyfikacja l¹d/woda
sm (soil moisture) - analiza wilgotnoœci gleby

dane testowe i treningowe znajduja sie w katalogu run/data/(ls|sm)
dane wejsciowe reprezentujace zdjecie z satelity skladaja sie z 2 plikow:
- *.bsq - zawierajacy obraz w formacie binarnym BSQ
- *.txt - plik opisucjacy obraz, w pierwszej linii znaduje sie liczba kanalow obrazu, w drugiej jego szerokosc, w trzeciej wysokosc
gdzie * to numer zdjecia (numeracja ci¹g³a, od 1)

katalog zawiera rozwiazanie w formie:
plik wykonywalny Solution.jar, przyjmujacy 2 parametry: sciezke do katalogu z analizowanymi danymi i typ przeprowadzanej analizy (ls|ms)
program generuje jako rozwiazanie obrazy w formacie .png i zapisuje je w katalogu results, ktory znajduje sie w katalogu z programem Solution.jar
