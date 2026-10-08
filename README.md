# `TreeMap` en Java

## 1. Descripción

`TreeMap<K, V>` es una implementación de la interfaz `Map` (paquete `java.util`) que almacena **pares clave-valor manteniendo siempre las claves ordenadas**. Internamente se basa en un **árbol rojo-negro**, una estructura de árbol binario de búsqueda auto-balanceada. Implementa las interfaces `SortedMap` y `NavigableMap`, lo que le da métodos de navegación y de rango que otros `Map` no tienen.

```
Map<K, V>  (interfaz)
├── HashMap
│   └── LinkedHashMap
├── SortedMap
│   └── NavigableMap
│       └── TreeMap   <-- foco de este documento
└── Hashtable (obsoleta)
```

El orden de las claves se define de dos formas:

- **Orden natural:** las claves implementan `Comparable` (por ejemplo `String`, `Integer`, `LocalDate`).
- **Orden personalizado:** se pasa un `Comparator` al constructor: `new TreeMap<>(Comparator.reverseOrder())`.

---

## 2. Características

| Característica | Descripción |
|---|---|
| **Claves siempre ordenadas** | Al recorrer el `TreeMap`, las claves salen en orden ascendente (o según el `Comparator`). |
| **Claves únicas** | Igual que todo `Map`: una clave repetida reemplaza el valor anterior. |
| **Estructura interna** | Árbol rojo-negro (auto-balanceado). |
| **Complejidad** | `put`, `get`, `remove` y `containsKey` son **O(log n)**. |
| **Sin claves `null`** | Lanza `NullPointerException` si se intenta usar una clave `null` con orden natural. Los valores `null` sí se permiten. |
| **Claves comparables** | Las claves deben implementar `Comparable` o se debe proporcionar un `Comparator`; de lo contrario lanza `ClassCastException`. |
| **Métodos de navegación** | `firstKey`, `lastKey`, `floorKey`, `ceilingKey`, `lowerKey`, `higherKey`, entre otros. |
| **Vistas por rango** | `headMap`, `tailMap` y `subMap` devuelven porciones del mapa. |
| **No sincronizado** | No es seguro para múltiples hilos sin protección adicional. |

> **Importante:** `TreeMap` determina si dos claves son iguales con `compareTo()`/`compare()` y **no** con `equals()`. Si el criterio de comparación es inconsistente con `equals()`, el comportamiento puede resultar inesperado.

---

## 3. Diferencias con otras implementaciones de `Map`

| Aspecto | `HashMap` | `LinkedHashMap` | `TreeMap` | `Hashtable` |
|---|---|---|---|---|
| **Estructura interna** | Tabla hash | Tabla hash + lista doblemente enlazada | Árbol rojo-negro | Tabla hash |
| **Orden de las claves** | Sin orden garantizado | Orden de inserción | **Ordenadas por clave** | Sin orden garantizado |
| **`get` / `put` / `remove`** | O(1) promedio | O(1) promedio | **O(log n)** | O(1) promedio |
| **Clave `null`** | Una permitida | Una permitida | **No permitida** | No permitida |
| **Valores `null`** | Permitidos | Permitidos | Permitidos | No permitidos |
| **Requisito de la clave** | `equals()` y `hashCode()` | `equals()` y `hashCode()` | `Comparable` o `Comparator` | `equals()` y `hashCode()` |
| **Métodos de rango y navegación** | No | No | **Sí** | No |
| **Sincronizado** | No | No | No | Sí (obsoleta) |
| **Consumo de memoria** | Menor | Mayor que `HashMap` | Mayor que `HashMap` (nodos del árbol) | Similar a `HashMap` |

**Resumen de la diferencia clave:** `HashMap` sacrifica el orden para ser más rápido; `LinkedHashMap` recuerda *cuándo* se insertó cada elemento; `TreeMap` mantiene las claves *ordenadas por valor* y permite consultas por rango, a cambio de ser más lento (O(log n)).

---

## 4. ¿Cuándo usar `TreeMap`?

| Caso de uso | Clave | Valor | Por qué `TreeMap` |
|---|---|---|---|
| **Diccionario o glosario ordenado** | Palabra | Definición | Se recorre en orden alfabético sin ordenar después. |
| **Ranking o tabla de posiciones** | Puntaje | Jugador | Se obtiene el mayor/menor con `lastKey()` / `firstKey()`. |
| **Escalas por rangos** (calificaciones, tramos de impuestos, descuentos) | Límite inferior del rango | Categoría | `floorEntry()` encuentra el rango al que pertenece un valor. |
| **Agenda o calendario** | Fecha/hora | Evento | Se consultan los eventos de un periodo con `subMap()`. |
| **Registro cronológico** (bitácora, historial de transacciones) | Marca de tiempo | Evento | Se mantiene ordenado por tiempo de forma automática. |
| **Búsqueda del valor más cercano** | Precio, distancia, ID | Producto o registro | `floorKey()` y `ceilingKey()` devuelven el vecino inferior o superior. |
| **Reportes ordenados** | Categoría, código | Total acumulado | La salida ya sale ordenada. |

**Úsalo cuando:**
- Necesitas recorrer los datos **siempre ordenados por clave**.
- Necesitas **consultas por rango** ("todo entre X y Y") o **el elemento más cercano** a un valor.
- Necesitas obtener rápidamente el **mínimo o máximo** de las claves.

**Mejor evita `TreeMap` cuando:**
- Solo necesitas buscar por clave y el orden no importa (usa `HashMap`, que es más rápido).
- Necesitas conservar el orden de inserción (usa `LinkedHashMap`).
- Las claves son objetos sin un criterio natural de comparación y no quieres definir un `Comparator`.
- Necesitas claves `null`.

---

## 5. Métodos principales

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

## 6. Ejemplos en código

### 6.1 Orden natural y orden personalizado

```java
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class OrdenTreeMap {
    public static void main(String[] args) {

        // Orden natural (alfabético) de las claves
        Map<String, Integer> puntajes = new TreeMap<>();
        puntajes.put("Luis", 85);
        puntajes.put("Ana", 92);
        puntajes.put("Marta", 78);

        System.out.println("Orden natural:");
        for (Map.Entry<String, Integer> par : puntajes.entrySet()) {
            System.out.println(par.getKey() + " -> " + par.getValue());
        }

        // Orden personalizado: inverso
        Map<String, Integer> inverso = new TreeMap<>(Comparator.reverseOrder());
        inverso.putAll(puntajes);

        System.out.println("\nOrden inverso:");
        for (Map.Entry<String, Integer> par : inverso.entrySet()) {
            System.out.println(par.getKey() + " -> " + par.getValue());
        }
    }
}
```

**Salida:**

```
Orden natural:
Ana -> 92
Luis -> 85
Marta -> 78

Orden inverso:
Marta -> 78
Luis -> 85
Ana -> 92
```

### 6.2 Escala por rangos con `floorEntry` (calificaciones)

```java
import java.util.TreeMap;

public class EscalaCalificaciones {
    public static void main(String[] args) {

        // Clave: límite inferior del rango | Valor: categoría
        TreeMap<Integer, String> escala = new TreeMap<>();
        escala.put(0, "Reprobado");
        escala.put(60, "Suficiente");
        escala.put(70, "Regular");
        escala.put(80, "Bueno");
        escala.put(90, "Excelente");

        int[] calificaciones = {45, 60, 85, 100};

        for (int calif : calificaciones) {
            // floorEntry: entrada con la mayor clave <= calif
            String categoria = escala.floorEntry(calif).getValue();
            System.out.println(calif + " -> " + categoria);
        }
    }
}
```

**Salida:**

```
45 -> Reprobado
60 -> Suficiente
85 -> Bueno
100 -> Excelente
```

### 6.3 Navegación y consultas por rango (catálogo por precio)

```java
import java.util.TreeMap;

public class CatalogoPrecios {
    public static void main(String[] args) {

        // Clave: precio | Valor: producto
        TreeMap<Double, String> catalogo = new TreeMap<>();
        catalogo.put(49.90, "Cable USB");
        catalogo.put(199.00, "Audífonos");
        catalogo.put(350.00, "Teclado");
        catalogo.put(899.00, "Monitor");
        catalogo.put(1500.00, "Impresora");

        System.out.println("Más barato: " + catalogo.firstEntry().getValue());
        System.out.println("Más caro: " + catalogo.lastEntry().getValue());

        // Producto más cercano sin pasarse de un presupuesto de 400
        System.out.println("Con 400 alcanza: " + catalogo.floorEntry(400.0).getValue());

        // Productos entre 100 y 900 (ambos incluidos)
        System.out.println("\nProductos entre $100 y $900:");
        catalogo.subMap(100.0, true, 900.0, true)
                .forEach((precio, producto) ->
                        System.out.println("$" + precio + " -> " + producto));
    }
}
```

**Salida:**

```
Más barato: Cable USB
Más caro: Impresora
Con 400 alcanza: Teclado

Productos entre $100 y $900:
$199.0 -> Audífonos
$350.0 -> Teclado
$899.0 -> Monitor
```

> Para usar métodos como `floorEntry`, `firstEntry` o `subMap`, la variable debe declararse como `TreeMap` (o `NavigableMap`), no como `Map`.

---

## 7. Ventajas y limitaciones

| Ventajas | Limitaciones |
|---|---|
| Claves siempre ordenadas sin necesidad de ordenar manualmente | Más lento que `HashMap` (O(log n) frente a O(1)) |
| Consultas por rango y de vecino más cercano | No acepta claves `null` |
| Acceso inmediato al menor y mayor elemento | Requiere claves comparables o un `Comparator` |
| Orden personalizable con `Comparator` | Mayor consumo de memoria que `HashMap` |
| Rendimiento predecible (el árbol se mantiene balanceado) | No es seguro para hilos sin sincronización adicional |

---

## 8. Referencias

Oracle. (s. f.). *TreeMap (Java SE 21 & JDK 21)*. Oracle Help Center. https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/TreeMap.html

Oracle. (s. f.). *The Java tutorials: Collections — The Map interface*. https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html
