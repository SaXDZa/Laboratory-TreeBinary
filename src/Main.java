import java.util.Scanner;

public class Main {

    private static void mostrarMenu() { //Muestra el menu que piden
        System.out.println();
        System.out.println("========== MENU ==========");
        System.out.println("1. Construir arbol");
        System.out.println("2. Mostrar arbol");
        System.out.println("3. Recorrido Preorden");
        System.out.println("4. Recorrido Inorden");
        System.out.println("5. Recorrido Posorden");
        System.out.println("6. Mostrar factores de equilibrio");
        System.out.println("7. Balancear arbol (AVL)");
        System.out.println("8. Salir");
        System.out.println("==========================");
        System.out.print("Seleccione una opcion: ");
    }

    private static int leerOpcion(Scanner sc) { //Lee la opcion como texto y la convierte en un numero
        //Si no es un numero devuelve -1; si ya no hay entrada, se sale del programa.
        if (!sc.hasNextLine()) {
            return 8;
        }
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {

        TreeNode a = new TreeNode("A");
        BinaryTree tree = new BinaryTree(a);

        a.left = new TreeNode("B");
        a.right = new TreeNode("C");
        a.left.left = new TreeNode("D");
        a.left.right = new TreeNode("E");
        a.right.left = new TreeNode("F");
        a.right.right = new TreeNode("G");
        a.left.left.left = new TreeNode("H");
        a.left.left.right = new TreeNode("I");

        System.out.println("Se cargo el arbol de ejemplo (A a I). Con la opcion 1 puede construir uno nuevo.");

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = leerOpcion(sc);

            if (opcion >= 2 && opcion <= 7 && tree.root == null) { // La opcion 2 al 7, se necesita tener un arbl construido
                System.out.println("El arbol esta vacio. Use la opcion 1 para construirlo.");
                continue;
            }

            switch (opcion) {
                case 1:
                    tree.buildTree(sc);
                    if (tree.root != null) {
                        System.out.println("\nArbol construido:");
                        tree.printTree();
                    }
                    break;
                case 2:
                    tree.printTree();
                    break;
                case 3:
                    System.out.println("Preorden:  " + tree.preorder());
                    break;
                case 4:
                    System.out.println("Inorden:   " + tree.inorder());
                    break;
                case 5:
                    System.out.println("Posorden:  " + tree.postorder());
                    break;
                case 6:
                    tree.printBalanceFactors();
                    break;
                case 7:
                    tree.balance();
                    break;
                case 8:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion no valida. Elija un numero del 1 al 8.");
            }
        } while (opcion != 8);
    }
}