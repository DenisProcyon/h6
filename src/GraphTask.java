import java.util.*;

/** Container class to different classes, that makes the whole
 * set of classes one class formally.
 */
public class GraphTask {

   /** Main method. */
   public static void main(int n, int m) {
      GraphTask a = new GraphTask();
      a.run(n, m);
   }

   public List<Arc> run_test2(){
      Graph g = new Graph("G2");

      Vertex v1 = g.createVertex("v1");
      Vertex v2 = g.createVertex("v2");

      List<Arc> shortestPath = g.findShortestPath(v1, v2);

      return shortestPath;
   }

   public List<Arc> run_test1(){
      Graph g = new Graph("G1");

      Vertex v1 = g.createVertex("v1");
      Vertex v2 = g.createVertex("v2");
      Vertex v3 = g.createVertex("v3");

      Arc av1_v2 = g.createArc("av1_v2", v1, v2);
      Arc av2_v3 = g.createArc("av2_v3", v2, v3);
      Arc av1_v3 = g.createArc("av1_v3", v1, v3);

      List<Arc> shortestPath = g.findShortestPath(v1, v2);

      return  shortestPath;
   }

   /** Actual main method to run examples and everything. */
   public List<Arc> run(int n, int m) {
      Graph g = new Graph("G");
      g.createRandomSimpleGraph(n, m);
      System.out.println(g);

      Vertex start = g.first;
      Vertex end = g.first.next.next.next.next.next;

      //int targetVertexNumber = 5;
      //int currentVertexNumber = 1;

      //while (end != null && currentVertexNumber < targetVertexNumber) {
      //   end = end.next;
      //   currentVertexNumber++;
      //}

      //if (end == null) {
       //  System.out.println("No vertex with number " + targetVertexNumber + " found.");
      //} else {
       //  System.out.println("Vertex with number " + targetVertexNumber + " is: " + end);
      //}

      List<Arc> shortestPath = g.findShortestPath(start, end);

      if (shortestPath == null) {
         System.out.println("No path exists between the given vertices.");
      } else {
         for (Arc arc : shortestPath) {
            System.out.println(arc);
         }
      }
      return shortestPath;
   }

   class Vertex {

      private String id;
      private Vertex next;
      private Arc first;
      private int info = 0;
      // You can add more fields, if needed

      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }
   }


   /** Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;
      private int info = 0;
      // You can add more fields, if needed

      Arc (String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }
   } 


   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;
      // You can add more fields, if needed

      Graph (String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph (String s) {
         this (s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty ("line.separator");
         StringBuffer sb = new StringBuffer (nl);
         sb.append (id);
         sb.append (nl);
         Vertex v = first;
         while (v != null) {
            sb.append (v.toString());
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append (" ");
               sb.append (a.toString());
               sb.append (" (");
               sb.append (v.toString());
               sb.append ("->");
               sb.append (a.target.toString());
               sb.append (")");
               a = a.next;
            }
            sb.append (nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         return res;
      }

      public Arc createArc (String aid, Vertex from, Vertex to) {
         Arc res = new Arc (aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * @param n number of vertices added to this graph
       */
      public void createRandomTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + String.valueOf(n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               createArc ("a" + varray [vnr].toString() + "_"
                  + varray [i].toString(), varray [vnr], varray [i]);
               createArc ("a" + varray [i].toString() + "_"
                  + varray [vnr].toString(), varray [i], varray [vnr]);
            } else {}
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int [info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res [i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraph (int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException 
               ("Impossible number of edges: " + m);
         first = null;
         createRandomTree (n);       // n-1 edges created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j) 
               continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0) 
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected [j][i] = 1;
            edgeCount--;  // a new edge happily created
         }
      }


      /**
       * Finds the shortest path between two vertices in the graph using Breadth-First Search.
       *
       * @param start The starting vertex for the search.
       * @param end   The ending vertex for the search.
       * @return A list of arcs representing the shortest path between the two vertices, or null if no path exists.
       */
      public List<Arc> findShortestPath(Vertex start, Vertex end) {
         // Check for null values of vertices
         if (start == null || end == null) {
            return null;
         }
         // Initialize previous arcs, queue, and visited vertices
         Map<Vertex, Arc> previousArcs = new HashMap<>();
         Queue<Vertex> queue = new LinkedList<>();
         Set<Vertex> visited = new HashSet<>();

         // Add the starting vertex to the queue and the set of visited vertices
         queue.offer(start);
         visited.add(start);

         // Breadth-first traversal of the graph
         while (!queue.isEmpty()) {
            Vertex currentVertex = queue.poll();

            // If the current vertex is the end vertex, break the loop
            if (currentVertex == end) {
               break;
            }

            // Iterate over all adjacent arcs of the current vertex
            Arc arc = currentVertex.first;
            while (arc != null) {
               Vertex targetVertex = arc.target;

               // If the adjacent vertex has not been visited, add it to the queue and the set of visited vertices
               if (!visited.contains(targetVertex)) {
                  visited.add(targetVertex);
                  previousArcs.put(targetVertex, arc);
                  queue.offer(targetVertex);
               }
               arc = arc.next;
            }
         }

         // If there is no path between the start and end vertices, return null
         if (!previousArcs.containsKey(end)) {
            return null;
         }

         // Reconstruct the shortest path using the map of previous arcs
         List<Arc> path = new ArrayList<>();
         Vertex currentVertex = end;
         while (currentVertex != start) {
            Arc arc = previousArcs.get(currentVertex);
            path.add(arc);
            currentVertex = findVertexByArc(arc, currentVertex);
         }
         Collections.reverse(path);
         return path;
      }

      private Vertex findVertexByArc(Arc arc, Vertex targetVertex) {
         Vertex v = first;
         while (v != null) {
            Arc a = v.first;
            while (a != null) {
               if (a == arc && a.target == targetVertex) {
                  return v;
               }
               a = a.next;
            }
            v = v.next;
         }
         return null;

      }


   }

} 

