import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;

public class BinaryTree {
    TreeNode root;

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(TreeNode root) {
        this.root = root;
    }

    public void insertRoot(Object data) {
        if (root == null) {
            root = new TreeNode(data);
        }
    }

    public TreeNode search(Object data) {
        return searchRec(root, data);
    }

    private TreeNode searchRec(TreeNode node, Object data) { //Miramos primerooo el nodo y luego el subarbol izquierdo y
        // despues el derecho

        if (node == null) {
            return null;
        }
        if (Objects.equals(node.data, data)) {
            return node;
        }
        TreeNode enIzquierdo = searchRec(node.left, data);
        if (enIzquierdo != null) {
            return enIzquierdo;
        }
        return searchRec(node.right, data);
    }

    public boolean insertLeft(Object parent, Object data) {
        //Agrege el "data" como hijo izquierdo del nodo el cual su dato es "parent"
        TreeNode padre = search(parent);
        if (padre == null || padre.left != null) {
            return false; // si parent no existe o si ya tiene un hijo izquierdo
        }
        padre.left = new TreeNode(data);
        return true;
    }

    public boolean insertRight(Object parent, Object data) { // Lo mismo que el anterior pero ahora con el derecho
        TreeNode padre = search(parent);
        if (padre == null || padre.right != null) {
            return false;
        }
        padre.right = new TreeNode(data);
        return true;
    }

    private String leerDato(Scanner scanner, String mensaje) {
        //Se lee un dato por consola, y nos devuelve null si esta vacio o si ya no hay mas entradas.
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            return null;
        }
        String texto = scanner.nextLine().trim();
        if (texto.isEmpty() || texto.equals("-")) {
            return null;
        }
        return texto;
    }

    public void buildTree(Scanner scanner) { //Me construye el arbol por niveles, primero la raiz y luego cada nodo
        root = null;
        String dato = leerDato(scanner, "Raiz: ");
        if (dato == null) {
            System.out.println("No se ingreso raiz. El arbol queda vacio.");
            return;
        }
        insertRoot(dato);

        Queue<TreeNode> cola = new LinkedList<>();
        cola.add(root);

        while (!cola.isEmpty()) {
            TreeNode actual = cola.remove();

            String izq = leerDato(scanner, "Hijo izquierdo de " + actual.data + ": ");
            if (izq != null) {
                actual.left = new TreeNode(izq);
                cola.add(actual.left);
            }

            String der = leerDato(scanner, "Hijo derecho de " + actual.data + ": ");
            if (der != null) {
                actual.right = new TreeNode(der);
                cola.add(actual.right);
            }
        }
    }

    private static class Bloque {
        List<String> lineas = new ArrayList<>();
        int ancho;
        int centro;//Esta es la "columna" donde esta el centro del rotulo de la raiz del bloque
    }

    private static String espacios(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static String rellenar(String texto, int ancho) {
        return texto + espacios(ancho - texto.length());
    }

    private Bloque construirBloque(TreeNode nodo, boolean mostrarFactor) {
        String rotulo = String.valueOf(nodo.data);
        if (mostrarFactor) {
            rotulo = rotulo + "(" + balanceFactor(nodo) + ")";
        }
        int largo = rotulo.length();
        Bloque bloque = new Bloque();

        if (nodo.left == null && nodo.right == null) { //Una hoja en un solo rotulo
            bloque.lineas.add(rotulo);
            bloque.ancho = largo;
            bloque.centro = largo / 2;
            return bloque;
        }

        Bloque izq = (nodo.left != null) ? construirBloque(nodo.left, mostrarFactor) : null;
        Bloque der = (nodo.right != null) ? construirBloque(nodo.right, mostrarFactor) : null;

        int separacion = 3;
        int centroIzq = 0;
        int centroDer = 0;
        int anchoHijos;
        List<String> lineasHijos = new ArrayList<>();

        if (izq != null && der != null) {
            centroIzq = izq.centro;
            centroDer = izq.ancho + separacion + der.centro;
            anchoHijos = izq.ancho + separacion + der.ancho;
            int filas = Math.max(izq.lineas.size(), der.lineas.size());
            for (int i = 0; i < filas; i++) {
                String a = i < izq.lineas.size() ? izq.lineas.get(i) : espacios(izq.ancho);
                String b = i < der.lineas.size() ? der.lineas.get(i) : espacios(der.ancho);
                lineasHijos.add(a + espacios(separacion) + b);
            }
        } else if (izq != null) {
            centroIzq = izq.centro;
            anchoHijos = izq.ancho;
            lineasHijos.addAll(izq.lineas);
        } else {
            centroDer = der.centro;
            anchoHijos = der.ancho;
            lineasHijos.addAll(der.lineas);
        }

        int centroPadre;
        if (izq != null && der != null) {
            centroPadre = (centroIzq + centroDer) / 2;
        } else if (izq != null) {
            centroPadre = centroIzq + 1;
        } else {
            centroPadre = centroDer - 1;
        }
        int inicio = centroPadre - largo / 2;

        int desplazamiento = Math.max(0, -inicio);
        inicio += desplazamiento;
        centroIzq += desplazamiento;
        centroDer += desplazamiento;
        int ancho = Math.max(anchoHijos + desplazamiento, inicio + largo);

        char[] filaRotulo = espacios(ancho).toCharArray();
        char[] filaRamas = espacios(ancho).toCharArray();
        for (int i = 0; i < largo; i++) {
            filaRotulo[inicio + i] = rotulo.charAt(i);
        }
        if (izq != null) {
            filaRamas[centroIzq] = '/';
            for (int c = centroIzq + 1; c < inicio; c++) {
                filaRotulo[c] = '_';
            }
        }
        if (der != null) {
            filaRamas[centroDer] = '\\';
            for (int c = inicio + largo; c < centroDer; c++) {
                filaRotulo[c] = '_';
            }
        }

        bloque.lineas.add(new String(filaRotulo));
        bloque.lineas.add(new String(filaRamas));
        for (String linea : lineasHijos) {
            bloque.lineas.add(rellenar(espacios(desplazamiento) + linea, ancho));
        }
        bloque.ancho = ancho;
        bloque.centro = inicio + largo / 2;
        return bloque;
    }

    private void dibujar(boolean mostrarFactor) {
        if (root == null) {
            System.out.println("(El arbol esta vacio)");
            return;
        }
        Bloque bloque = construirBloque(root, mostrarFactor);
        for (String linea : bloque.lineas) {
            int fin = linea.length();
            while (fin > 0 && linea.charAt(fin - 1) == ' ') {
                fin--;
            }
            System.out.println(linea.substring(0, fin));
        }
    }

    public void printTree() { //Nos permite mostrar el arbol graficamente en consola
        dibujar(false);
    }

    private void preorderRec(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(node.data).append(' ');//raiz
        preorderRec(node.left, sb);//izquierdo
        preorderRec(node.right, sb);//derecho
    }

    private void inorderRec(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inorderRec(node.left, sb);//izquierdo
        sb.append(node.data).append(' ');//raiz
        inorderRec(node.right, sb);//derecho
    }

    private void postorderRec(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        postorderRec(node.left, sb);// izquierdo
        postorderRec(node.right, sb);// derecho
        sb.append(node.data).append(' ');// raiz
    }

    private String cerrar(StringBuilder sb) { //Quita el espacio que nos queda al final de la secuencia
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public String preorder() {
        StringBuilder sb = new StringBuilder();
        preorderRec(root, sb);
        return cerrar(sb);
    }

    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorderRec(root, sb);
        return cerrar(sb);
    }

    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorderRec(root, sb);
        return cerrar(sb);
    }

    public int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int balanceFactor(TreeNode node) { //Para el factor de equilibrio = altura derecha - altura izquierda
        if (node == null) {
            return 0;
        }
        return height(node.right) - height(node.left);
    }

    // true si TODOS los nodos tienen factor -1, 0 o 1.
    public boolean isBalanced() { // nos da true si absolutamente todoos los nodos tienen factor -1, 0 o 1.
        return isBalancedRec(root);
    }

    private boolean isBalancedRec(TreeNode node) {
        if (node == null) {
            return true;
        }
        int fe = balanceFactor(node);
        if (fe < -1 || fe > 1) {
            return false;
        }
        return isBalancedRec(node.left) && isBalancedRec(node.right);
    }

    public List<Object> unbalancedNodes() {
        List<Object> lista = new ArrayList<>();
        recogerDesbalanceados(root, lista);
        return lista;
    }

    private void recogerDesbalanceados(TreeNode node, List<Object> lista) {
        if (node == null) {
            return;
        }
        int fe = balanceFactor(node);
        if (fe < -1 || fe > 1) {
            lista.add(node.data);
        }
        recogerDesbalanceados(node.left, lista);
        recogerDesbalanceados(node.right, lista);
    }

    public void printBalanceFactors() { // Nos muestra el arbol con el factor de equilibrio de cada nodo entre parentesis.
        System.out.println("(Entre parentesis: factor de equilibrio = altura derecha - altura izquierda)");
        dibujar(true);
    }

    public TreeNode rotateRight(TreeNode node) {  // Rotacion simple a la derecha (caso izquierda-izquierda).
        if (node == null || node.left == null) {
            return node;
        }
        TreeNode nuevaRaiz = node.left;
        node.left = nuevaRaiz.right;//el hijo derecho de la nueva raiz pasa a ser hijo izquierdo de node
        nuevaRaiz.right = node;//el node baja a la derecha
        return nuevaRaiz;
    }

    public TreeNode rotateLeft(TreeNode node) { // Rotacion simple a la izquierda (caso derecha-derecha).
        if (node == null || node.right == null) {
            return node;
        }
        TreeNode nuevaRaiz = node.right;
        node.right = nuevaRaiz.left;//el hijo izquierdo de la nueva raiz pasa a ser hijo derecho de node
        nuevaRaiz.left = node;//el node baja a la izquierda
        return nuevaRaiz;
    }

    public TreeNode rotateLeftRight(TreeNode node) { //Rotacion doble izquierda-derecha: primero a la izquierda
        // sobre el hijo izquierdo y luego a la derecha sobre el nodo.
        if (node == null) {
            return null;
        }
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    public TreeNode rotateRightLeft(TreeNode node) { //Rotacion doble derecha-izquierda: primero a la derecha
        // sobre el hijo derecho y luego a la izquierda sobre el nodo.
        if (node == null) {
            return null;
        }
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    private TreeNode balanceRec(TreeNode node) { //Balanceamos de abajo hacia arriba: primero los hijos y luego el nodo.
        if (node == null) {
            return null;
        }
        node.left = balanceRec(node.left);
        node.right = balanceRec(node.right);

        int fe = balanceFactor(node);

        if (fe < -1) {//nos pesa de mas el lado izquierdo
            if (balanceFactor(node.left) <= 0) {
                System.out.println("  - Rotacion simple a la derecha en " + node.data);
                return rotateRight(node);
            }
            System.out.println("  - Rotacion doble izquierda-derecha en " + node.data);
            return rotateLeftRight(node);
        }
        if (fe > 1) {//nos  pesa de mas el lado derecho
            if (balanceFactor(node.right) >= 0) {
                System.out.println("  - Rotacion simple a la izquierda en " + node.data);
                return rotateLeft(node);
            }
            System.out.println("  - Rotacion doble derecha-izquierda en " + node.data);
            return rotateRightLeft(node);
        }
        return node;
    }

    public void balance() { //Nos muestra el arbol original, identifica los nodos desbalanceados,
        //aplica las rotaciones necesarias y muestra el arbol resultante.
        if (root == null) {
            System.out.println("(El arbol esta vacio)");
            return;
        }

        System.out.println("=== Arbol ORIGINAL ===");
        printBalanceFactors();

        List<Object> desbalanceados = unbalancedNodes();
        if (desbalanceados.isEmpty()) {
            System.out.println("\nTodos los nodos tienen factor -1, 0 o 1: el arbol ya es AVL.");
            return;
        }
        System.out.println("\nNodos que requieren balanceo: " + desbalanceados);

        System.out.println("Rotaciones aplicadas:");
        int vueltas = 0;
        while (!isBalanced() && vueltas < 100) {
            root = balanceRec(root);
            vueltas++;
        }

        System.out.println("\n=== Arbol BALANCEADO (AVL) ===");
        printBalanceFactors();
        if (!isBalanced()) {
            System.out.println("Aviso: no se logro balancear por completo.");
        }
    }
}
