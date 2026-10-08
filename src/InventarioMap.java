
import java.util.HashMap;
import java.util.Map;

public class InventarioMap {
    public static void main(String[] args) {

        // Clave: código del producto | Valor: existencias
        Map<String, Integer> inventario = new HashMap<>();

        // 1. Agregar pares (put)
        inventario.put("P001", 25);
        inventario.put("P002", 10);
        inventario.put("P003", 40);

        System.out.println("\n--- Inventario actual ---");
        for (Map.Entry<String, Integer> par : inventario.entrySet()) {
            System.out.println(par.getKey() + " -> " + par.getValue());
        }

        System.out.println("\nProductos registrados: " + inventario.size());

        // 2. Consultar por clave (get)
        System.out.println("Existencias de P002: " + inventario.get("P002"));

        // 3. Actualizar un valor (put con clave existente reemplaza el valor)
        inventario.put("P002", 15);

        // 4. Verificar existencia de una clave
        if (inventario.containsKey("P004")) {
            System.out.println("P004 está registrado");
        } else {
            System.out.println("P004 no existe en el inventario");
        }

        // 5. Valor por defecto si la clave no existe
        int existencias = inventario.getOrDefault("P004", 0);
        System.out.println("Existencias de P004: " + existencias);

        // 6. Eliminar un par
        inventario.remove("P003");

        // 7. Recorrer todos los pares (entrySet)
        System.out.println("\n--- Inventario actual ---");
        for (Map.Entry<String, Integer> par : inventario.entrySet()) {
            System.out.println(par.getKey() + " -> " + par.getValue());
        }

        // 8. Tamaño del Map
        System.out.println("\nProductos registrados: " + inventario.size());
    }
}
