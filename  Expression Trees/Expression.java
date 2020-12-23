package Project3;
import java.util.Stack;
import java.util.Scanner;

public class Expression extends ExpressionTree {

   public Expression(String expr) {
      //Expression string is first formatted and placed in the center of the parentheses
      //then gets formatted and separate them using format method
      assert !expr.isEmpty();
      expr = "(" + expr + ")";
      expr = format(expr);
      expr = expr.trim().replaceAll("\\s+"," ");
      //after formatting, the expression gets split into smaller piece
      String[] exprArr = expr.split(" ");
      Stack<BNode<String>> operandStack = new Stack<>();
      Stack<String> operatorStack = new Stack<>();
      BNode<String> node;
      //For each loop used to traverse the expression starting from the beginning with "("
      // if exits adds to stack then check the next split string
      String ch ;
      for (String s : exprArr) {
         ch = s;
         if (ch.equals("(")) {
            operatorStack.push(ch);
            // if its digit
         } else if (Character.isDigit(ch.charAt(0))) {
            node = new BNode<>(ch + "", null, null, null);
            operandStack.push(node);
            //if +-*/ > 0
         } else  if( Character.isLetter((ch.charAt((0))))){
            node = new BNode<>(ch + "", null, null, null);
            operandStack.push(node);
         } else if (operatorPrecedence(ch.charAt(0)) > 0) {
            while (!operatorStack.peek().equals("(") && (operatorPrecedence(operatorStack.peek().charAt(0))
                    >= operatorPrecedence(ch.charAt(0)))){
               node = new BNode<>(operatorStack.pop() + "", null, null, null);
               node.setRight(operandStack.pop());
               node.setLeft(operandStack.pop());
               operandStack.push(node);
            }
            operatorStack.push(ch);
            //when character equals )
            //check if stack is empty or not and check if the stack beginning starts with (
            //if it is then set left/right node
            //then add it on to stack
         } else if (ch.equals( ")" )) {
            while (!operatorStack.peek().equals("(")) {
               node = new BNode<>(operatorStack.pop() + "", null, null, null);
               node.setRight(operandStack.pop());
               node.setLeft(operandStack.pop());
               operandStack.push(node);
            }
            operatorStack.pop();
         }
      }
      root = operandStack.pop();
   }

   //use to determine which operator is higher
   public static int operatorPrecedence(char ch) {
      if (ch == '+' || ch == '-') {
         return 1;
      }
      else if(ch == '/' || ch == '*') {
         return 2;
      }
      return 0;
   }

   //Recursive method
   public String fullyParenthesized() {
      StringBuilder sb = new StringBuilder();
      fullyParenthesized(root, sb);
      return sb.toString();
   }

   //help method to determine, where to add parenthesized
   //and add data into BNode
   private void fullyParenthesized(Node<String> r, StringBuilder sb){
      BNode<String> root = (BNode<String>)r;
      if (root != null) {
         if (root.left != null && root.right != null) {
            sb.append("(");
         }
         fullyParenthesized(root.left, sb);
         sb.append(root.getData());
         fullyParenthesized(root.right, sb);
         if (root.left != null && root.right != null) {
            sb.append(")");
         }
      }
   }

   //Helper method to format the string expression
   //set white spaces
   private static String format(String expression) {
      expression = expression.replace("(", " ( ");
      expression = expression.replace(")", " ) ");
      expression = expression.replace("+", " + ");
      expression = expression.replace("-", " - ");
      expression = expression.replace("*", " * ");
      expression = expression.replace("/", " / ");
      return expression;

   }


   public double evaluate() {
      //get full expression string
      String expr = fullyParenthesized();
      //check if expr is empty or null : not necessary needed
      assert !expr.isEmpty();
      //format the expression string with correct whitespaces
      expr = format(expr);
      Stack<Character> operators = new Stack<>();
      Stack<Double> operands = new Stack<>();
      Scanner scanner = new Scanner(expr);
      while (scanner.hasNext()) {
         if (scanner.hasNextDouble()) {
            //when scanner string is value, adds it into value and push the value into stack
            double value = scanner.nextDouble();
            operands.push(value);
         } else {
            // when scanner string is operators { + - / * }
            // first always check for ")" if it is
            //the operations will continue--->  later check for either + / * -
            char operator = scanner.next().charAt(0);
            switch(operator){
               case'(':
                  continue;
               case '+':
               case '-':
               case '*':
               case '/':
                  //pushes it
                  operators.push(operator);
                  break;
                  //when expression ")" , or the end
               case ')':
                  // this is when stack pops out the values
                  double num_1 = operands.pop();
                  double num_2 = operands.pop();
                  char arithmeticOperators = operators.pop();
                  double result = 0;
                  if (arithmeticOperators == '+') {
                     result = num_2 + num_1;
                  }
                  if (arithmeticOperators == '-') {
                     result = num_2 - num_1;
                  }
                  if (arithmeticOperators == '*') {
                     result = num_2 * num_1;
                  }
                  if (arithmeticOperators == '/') {
                     result = num_2 / num_1;
                  }
                  //get the result
                  operands.push(result);
                  break;
            }
         }
      }
      //pops the recently added element out of the stack
      return operands.pop();
   }
}
