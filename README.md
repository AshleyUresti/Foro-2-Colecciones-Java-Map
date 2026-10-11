# `TreeMap` en Java

## ¿Qué es?

`TreeMap<K, V>` es una implementación de la interfaz `Map` (paquete `java.util`) que almacena **pares clave-valor manteniendo siempre las claves ordenadas**. Internamente se basa en un **árbol rojo-negro**, una estructura de árbol binario de búsqueda auto-balanceada. Implementa las interfaces `SortedMap` y `NavigableMap`, lo que le da métodos de navegación y de rango que otros `Map` no tienen.

```
Map<K, V>  (interfaz)
├── HashMap
│   └── LinkedHashMap
├── SortedMap
│   └── NavigableMap
│       └── TreeMap   <-- 
└── Hashtable (obsoleta)
```

El orden de las claves se define de dos formas:

- **Orden natural:** las claves implementan `Comparable` (por ejemplo `String`, `Integer`, `LocalDate`).
- **Orden personalizado:** se pasa un `Comparator` al constructor: `new TreeMap<>(Comparator.reverseOrder())`.

---

| Característica | Descripción |
|---|---|
| **Claves siempre ordenadas** | Al recorrer el `TreeMap`, las claves salen en orden ascendente (o según el `Comparator`). |
| **Claves únicas** | Igual que todo `Map`: una clave repetida reemplaza el valor anterior. |
| **Estructura interna** | Árbol rojo-negro (auto-balanceado). |
| **Métodos de navegación** | `firstKey`, `lastKey`, `floorKey`, `ceilingKey`, `lowerKey`, `higherKey`, entre otros. |
| **Vistas por rango** | `headMap`, `tailMap` y `subMap` devuelven porciones del mapa. |

---

## Diferencias con otras implementaciones de `Map`

| Aspecto | `HashMap` | `LinkedHashMap` | `TreeMap` | `Hashtable` |
|---|---|---|---|---|
| **Estructura interna** | Tabla hash | Tabla hash + lista doblemente enlazada | Árbol rojo-negro | Tabla hash |
| **Orden de las claves** | Sin orden garantizado | Orden de inserción | Ordenadas por clave | Sin orden garantizado |
| **`get` / `put` / `remove`** | O(1) promedio | O(1) promedio | O(log n) | O(1) promedio |
| **Clave `null`** | Una permitida | Una permitida | No permitida | No permitida |
| **Valores `null`** | Permitidos | Permitidos | Permitidos | No permitidos |
| **Requisito de la clave** | `equals()` y `hashCode()` | `equals()` y `hashCode()` | `Comparable` o `Comparator` | `equals()` y `hashCode()` |
| **Métodos de rango y navegación** | No | No | Sí | No |
| **Consumo de memoria** | Menor | Mayor que `HashMap` | Mayor que `HashMap` (nodos del árbol) | Similar a `HashMap` |


---

## ¿Cuándo usarlo?
- Necesitas recorrer los datos **siempre ordenados por clave**.
- Necesitas **consultas por rango** ("todo entre X y Y") o **el elemento más cercano** a un valor.
- Necesitas obtener rápidamente el **mínimo o máximo** de las claves.

---

## Métodos principales

| Método | Función |
|---|---|
| `put(K, V)` / `get(K)` / `remove(K)` | Operaciones básicas. |
| `firstKey()` / `lastKey()` | Menor / mayor clave. |
| `firstEntry()` / `lastEntry()` | Par con la menor / mayor clave. |
| `pollFirstEntry()` / `pollLastEntry()` | Devuelve y elimina el primer / último par. |
| `floorKey(K)` | Mayor clave **menor o igual** a la dada. |
| `ceilingKey(K)` | Menor clave **mayor o igual** a la dada. |
| `lowerKey(K)` / `higherKey(K)` | Mayor clave **estrictamente menor** / menor clave **estrictamente mayor**. |
| `headMap(K)` | Pares con claves menores que la dada. |
| `tailMap(K)` | Pares con claves mayores o iguales a la dada. |
| `subMap(K1, K2)` | Pares con claves en un rango. |
| `descendingMap()` | Vista del mapa en orden inverso. |

---

## Ejemplos en código

### Ranking

```Ranking.java
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
```
```Main.java
public class Main {
    public static void main(String[] args) {
        Ranking r = new Ranking();
        r.registrar("Ashe", 1800);
        r.registrar("Mein", 1900);
        r.registrar("Karlota", 1000);
        r.registrar("Renesil", 900);
        r.registrar("Isaelop", 1200);
        r.registrar("Vann", 700);

        r.mostrarTop(5);
        System.out.println("Ganador: " + r.campeon());

        System.out.println("\nRenesil hace una partida increíble...\n");
        r.registrar("Renesil", 2000);

        r.mostrarTop(5);
        System.out.println("Ganador: " + r.campeon());

        System.out.println("\nPosición de Vann: " + r.posicionDe("Vann"));
    }
}

```
**Salida:**

<img width="1025" height="540" alt="image" src="https://github.com/user-attachments/assets/d0fb21f8-2882-4f76-a5dd-43059b323bf6" />

## Ventajas y limitaciones

| Ventajas | Limitaciones |
|---|---|
| Claves siempre ordenadas sin necesidad de ordenar manualmente | Más lento que `HashMap` (O(log n) frente a O(1)) |
| Consultas por rango y de vecino más cercano | No acepta claves `null` |
| Acceso inmediato al menor y mayor elemento | Requiere claves comparables o un `Comparator` |
| Orden personalizable con `Comparator` | Mayor consumo de memoria que `HashMap` |
| Rendimiento predecible (el árbol se mantiene balanceado) | No es seguro para hilos sin sincronización adicional |

---

## Referencias

Oracle. (s. f.). *TreeMap (Java SE 21 & JDK 21)*. Oracle Help Center. https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/TreeMap.html

Oracle. (s. f.). *The Java tutorials: Collections — The Map interface*. https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html
