package csc210_project2;

import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrefixTrees extends Application
{
    @Override
    public void start(Stage stage)
    {
        // scene elements
        TextField input = new TextField();
        Label instruction = new Label("Enter a prefix expression here");
        Button buildTree = new Button("Build Prefix Tree");
        Button computeValue = new Button("Compute Value");
        Button convertToInfix = new Button("Convert To Infix");
        
        // create empty tree view
        TreeView<String> tree = new TreeView<>();
        tree.setVisible(false);
        
        // hbox to hold buttons
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(buildTree, computeValue, convertToInfix);
        hBox.setAlignment(Pos.CENTER);

        // vbox to hold and center elements
        VBox vBox = new VBox(10, tree, instruction, input, hBox);
        vBox.setPadding(new Insets(12, 50, 50, 50));
        vBox.setAlignment(Pos.BOTTOM_CENTER);

        // create scene with specified width and height
        Scene scene = new Scene(vBox, 500, 300);

        // create stage
        stage.setTitle("Prefix Trees Program");
        stage.setScene(scene);
        stage.show();

        // attach event handler to each button
        // buildTreeHandler requires TreeView to manipulate it on the stage        
        EventHandler buildTreeHandler = new BuildTreeListener(input, tree);
        buildTree.setOnAction(buildTreeHandler);

        EventHandler computeValueHandler = new ComputeValueListener(input);
        computeValue.setOnAction(computeValueHandler);

        EventHandler convertToInfixHandler = new ConvertToInfixListener(input);
        convertToInfix.setOnAction(convertToInfixHandler);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

// ------------------------------------------------------- //

class BuildTreeListener implements EventHandler<ActionEvent>
{
    TextField input;
    TreeView<String> tree;

    public BuildTreeListener (TextField tf, TreeView tree)
    {
        input = tf;
        this.tree = tree;
    }
    
    @Override
    public void handle(ActionEvent event) 
    {
        // obtain input text
        String expression = input.getText();
        // seperate by whitespace
        Scanner sc = new Scanner(expression).useDelimiter(" ");
        // make tree visible only when button is clicked
        tree.setVisible(true);
        // set root of tree through recursive function
        tree.setRoot(evaluateTree(sc));
    }

    public TreeItem<String> evaluateTree(Scanner sc)
    {
        
        TreeItem <String> operator;
        TreeItem <String> operand1;
        TreeItem <String> operand2;
        
        // base case
        if (sc.hasNextInt())
        {
            TreeItem<String> number = new TreeItem(sc.next());
            return number;
        }
        else
        {
            // change the operator symbol based on case
            switch (sc.next("[*+-/]"))
            {
                case "+":
                    // root 
                    operator = new TreeItem<>("+");
                    // children
                    operand1 = evaluateTree(sc);
                    operand2 = evaluateTree(sc);
                    // add children to root
                    operator.getChildren().addAll(operand1, operand2);
                    operator.setExpanded(true);
                    return operator;
                case "-":
                    operator = new TreeItem<>("-");
                    operand1 = evaluateTree(sc);
                    operand2 = evaluateTree(sc);
                    operator.getChildren().addAll(operand1, operand2);
                    operator.setExpanded(true);
                    return operator;
                case "*":                    
                    operator = new TreeItem<>("*");
                    operand1 = evaluateTree(sc);
                    operand2 = evaluateTree(sc);
                    operator.getChildren().addAll(operand1, operand2);
                    operator.setExpanded(true);
                    return operator;
                case "/":
                    operator = new TreeItem<>("/");                   
                    operand1 = evaluateTree(sc);
                    operand2 = evaluateTree(sc);
                    operator.getChildren().addAll(operand1, operand2);
                    operator.setExpanded(true);
                    return operator;
            }
        }
        
        // program should never reach this return statement
        TreeItem<String> blanktree = new TreeItem<>();
        return blanktree;
    }
}

// ------------------------------------------------------- //

class ComputeValueListener implements EventHandler<ActionEvent>
{
    TextField input;
    
    public ComputeValueListener (TextField tf)
    {
        input = tf;
    }
    
    @Override
    public void handle(ActionEvent event) 
    {
        // obtain input text
        String expression = input.getText();
        // seperate by whitespace
        Scanner sc = new Scanner(expression).useDelimiter(" ");
        
        // create alert that calls recursive function
        Alert alert = new Alert(AlertType.INFORMATION, 
                Integer.toString(evaluateExpression(sc)));
        alert.setTitle("Prefix Expression");
        alert.setHeaderText("The computed value of the expression is:");
        alert.showAndWait();
    }

    public int evaluateExpression(Scanner sc)
    {   
        int operand1;
        int operand2;
        
        // base case
        if (sc.hasNextInt())
        {
            return sc.nextInt();
        }
        else
        {
            // change operation based on symbol read from scanner
            switch (sc.next("[*+-/]"))
            {
                case "+":
                    // recursively evaluate operands
                    operand1 = evaluateExpression(sc);
                    operand2 = evaluateExpression(sc);
                    // perform operation
                    return operand1 + operand2;
                case "-":
                    operand1 = evaluateExpression(sc);
                    operand2 = evaluateExpression(sc);
                    return operand1 - operand2;
                case "*":
                    operand1 = evaluateExpression(sc);
                    operand2 = evaluateExpression(sc);
                    return operand1 * operand2;
                case "/":
                    operand1 = evaluateExpression(sc);
                    operand2 = evaluateExpression(sc);
                    return operand1 / operand2;
            }
        }
        
        // program should never reach this return statement
        return 0;
    }
}

// ------------------------------------------------------- //

class ConvertToInfixListener implements EventHandler<ActionEvent>
{
    TextField input;
    public ConvertToInfixListener (TextField tf)
    {
        input = tf;
    }

    @Override
    public void handle(ActionEvent event) 
    {
        // obtain input text
        String word = input.getText();
        // seperate by whitespace
        Scanner sc = new Scanner(word).useDelimiter(" ");
        
        // create alert that calls recursive function
        Alert alert = new Alert(AlertType.INFORMATION, 
                createInfixExpression(sc));
        alert.setTitle("Infix Expression");
        alert.setHeaderText("The infix expression is:");
        alert.showAndWait();
    }
    
    public String createInfixExpression(Scanner sc)
    {
        String operand1;
        String operand2;
        
        // base case
        if (sc.hasNextInt())
        {
            return sc.next();
        }
        else
        {
            // change the operator symbol based on case
            switch (sc.next("[*+-/]"))
            {
                case "+":
                    operand1 = createInfixExpression(sc);
                    operand2 = createInfixExpression(sc);
                    // place operation in parentheses with the
                    // appropriate operator symbol
                    return "(" + operand1 + " + " + operand2 + ")";
                case "-":
                    operand1 = createInfixExpression(sc);
                    operand2 = createInfixExpression(sc);
                    return "(" + operand1 + " - " + operand2 + ")";
                case "*":
                    operand1 = createInfixExpression(sc);
                    operand2 = createInfixExpression(sc);
                    return "(" + operand1 + " * " + operand2 + ")";
                case "/":
                    operand1 = createInfixExpression(sc);
                    operand2 = createInfixExpression(sc);
                    return "(" + operand1 + " / " + operand2 + ")";
            }
        }
        
        // program should never reach this return statement
        return "";
    }
}




