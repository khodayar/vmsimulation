import Models.*;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.Dimension;
import javax.swing.JFrame;
import com.google.common.graph.Network;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Runner {

    public static void main(String[] args) {



        Cloud current = new Cloud();
        current.addOptimalPlacement(4 , 12);
        current.assignRndNewLocations();

        //end of setting up the network

        current.showAssignments();


        DependencyGraph dependencyGraph;


        dependencyGraph = current.generateDependencyGraph(current.generateMigrations());

        //it will fill the list of migrations for the network
        current.generateMigrations();

//        //setting default migration weights
        current.setMigrationWeights(current.getMigrations());

        List<Migration> migrationsOFCurrent = current.getMigrations();

        System.out.println(current.getMigrations());
//
        dependencyGraph.printDependency();

        current.draw(dependencyGraph);

//
        current.solveCycles();
//
//
//        //
        dependencyGraph = current.generateDependencyGraph(current.getMigrations());
        //loop for cycles
        current.setDependencyWeights(current.getMigrations());
        //   System.out.println("Dependencies :");
        dependencyGraph.printDependency();
        //  System.out.println(dependencyGraph.getPath(set2 , set2));
        //  System.out.println(dependencyGraph.getPath(set5 , set5));
        //  System.out.println(current.getMigrations());
        //   System.out.println(current.getAllOutGoingSets(current.generateMigrations()));
        // System.out.println(dependencyGraph.getDependencyDept(set3 ,  new ArrayList<>(), 0,new ArrayList<>()));
        // System.out.println(dependencyGraph.returnChain(set1 , set4));
        //System.out.println(dependencyGraph.getPath(set1 , set5));

        MigrationProcess migrationProcess = new MigrationProcess();
        migrationProcess.setDegree(2);
        migrationProcess.setCloud(current);
        try {
            migrationProcess.doMigration();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
