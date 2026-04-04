Szymon Potępa

Uruchomienie symulacji: src/main/java/agh/ics/oop/app/WorldGUI

Wariant: Pory roku

Rozszerzenia:

-Uruchamianie wielu symulacji jednocześnie w osobnych okienkach

-Wizualizowanie energii zwierzaków (np. kolor/pasek pod obiektem) na żywo w trakcie symulacji

-Podglądanie statystyk danego zwierzaka . Po zatrzymaniu symulacji można zaznaczyć zwierzaka jako wybranego do
śledzenia. Od tego momentu (do zatrzymania śledzenia) UI powinien przekazywać nam informacje o jego statusie i historii:
jaki ma genom,
która jego część jest aktywowana,
ile ma energii,
ile zjadł roślin,
ile posiada dzieci,
ile posiada potomków (niekoniecznie będących bezpośrednio dziećmi), (brak)
ile dni już żyje (jeżeli żyje),
którego dnia zmarło (jeżeli żywot już skończyło).

-Wizualizacja wybranej statystyki na wykresie, zmieniającym się w trakcie symulacji, ważne, by dało się określić w
aplikacji, którą statystykę śledzimy (np. średnia energia zwierzaków albo średnia długość życia)
