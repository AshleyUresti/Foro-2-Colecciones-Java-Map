import java.util.*;

public class Ranking {

    // puntaje (de mayor a menor) -> jugadores con ese puntaje
    private final TreeMap<Integer, List<String>> ranking =
            new TreeMap<>(Comparator.reverseOrder());

    // jugador (clave) -> su puntaje actual (valor)
    //busqueda rapida por nombre
    private final Map<String, Integer> puntajes = new HashMap<>();

    public void registrar(String jugador, int puntaje) {
        Integer anterior = puntajes.get(jugador);
        if (anterior != null) {                       // ya estaba, lo sacamos de su puntaje viejo
            List<String> lista = ranking.get(anterior);
            lista.remove(jugador);
            if (lista.isEmpty()) ranking.remove(anterior);
        }
        puntajes.put(jugador, puntaje);
        ranking.computeIfAbsent(puntaje, k -> new ArrayList<>()).add(jugador);
    }

    public void mostrarTop(int n) {
        System.out.println("=== TOP " + n + " ===");
        int posicion = 1, mostrados = 0;
        for (Map.Entry<Integer, List<String>> grupo : ranking.entrySet()) {
            for (String jugador : grupo.getValue()) {
                if (mostrados == n) return;
                System.out.println(posicion + ". " + jugador + " - " + grupo.getKey() + " pts");
                mostrados++;
            }
            posicion += grupo.getValue().size(); // los empatados comparten posición
        }
    }

    public int posicionDe(String jugador) {
        Integer p = puntajes.get(jugador);
        if (p == null) return -1;
        int mejores = 0;
        for (List<String> lista : ranking.headMap(p).values()) { // puntajes MAYORES al suyo
            mejores += lista.size();
        }
        return mejores + 1;
    }

    public String campeon() {
        return ranking.firstEntry().getValue().toString();
    }
}
