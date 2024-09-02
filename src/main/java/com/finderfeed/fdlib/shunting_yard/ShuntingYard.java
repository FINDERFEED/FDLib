package com.finderfeed.fdlib.shunting_yard;

import com.finderfeed.fdlib.shunting_yard.functions.AdditionFunction;
import com.finderfeed.fdlib.shunting_yard.functions.DivisionFunction;
import com.finderfeed.fdlib.shunting_yard.functions.MultiplicationFunction;
import com.finderfeed.fdlib.shunting_yard.functions.SubtractionFunction;
import com.finderfeed.fdlib.shunting_yard.sy_base.SYFunction;
import com.finderfeed.fdlib.shunting_yard.sy_base.SYNamedVariable;
import com.finderfeed.fdlib.shunting_yard.sy_base.SYNode;
import com.finderfeed.fdlib.shunting_yard.sy_base.SYStaticValue;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShuntingYard {

    public static Map<String,SYFunction> OPERATORS = new HashMap<>(Map.of(
            "-",new SubtractionFunction(),
            "+",new AdditionFunction(),
            "*",new MultiplicationFunction(),
            "/",new DivisionFunction()
    ));

    public static Map<String,Integer> OPERATOR_PRIORITY = new HashMap<>(Map.of(
            "-",2,
            "+",2,
            "*",3,
            "/",3
    ));


    public static RPNExpression parse(String expression){
        expression = expression.replace(" ","");
        ReadResult readResult;
        StringBuilder builder = new StringBuilder(expression);
        Stack<ReadResult> readResults = new Stack<>();
        List<SYNode> result = new ArrayList<>();
        AtomicBoolean prevOperator = new AtomicBoolean(true);
        AtomicBoolean minus = new AtomicBoolean(false);
        while ((readResult = next(builder)) != null){
            handleReadResult(readResult,readResults,result,prevOperator,minus);
        }
        while (!readResults.isEmpty()){
            SYFunction operator = OPERATORS.get(readResults.pop().str);
            result.add(operator);
        }
        return new RPNExpression(result);

    }

    private static void handleReadResult(ReadResult readResult, Stack<ReadResult> readResults, List<SYNode> result, AtomicBoolean wasPreviousAnOperator,AtomicBoolean unaryMinus){

        switch (readResult.type){
            case NUMBER -> {
                float fl = Float.parseFloat(readResult.str);
                result.add(new SYStaticValue(fl));
                if (unaryMinus.get()){
                    result.add(new SubtractionFunction());
                    unaryMinus.set(false);
                }
                wasPreviousAnOperator.set(false);
            }
            case VARIABLE -> {
                result.add(new SYNamedVariable(readResult.str));
                if (unaryMinus.get()){
                    result.add(new SubtractionFunction());
                    unaryMinus.set(false);
                }
                wasPreviousAnOperator.set(false);
            }
            case OPERATOR -> {
                if (readResult.str.equals("-") && wasPreviousAnOperator.get()){
                    unaryMinus.set(true);
                    result.add(new SYStaticValue(0));
                }else {
                    handleOperatorToken(readResult, readResults, result);
                }
                wasPreviousAnOperator.set(true);
            }
            case FUNCTION -> {
                if (unaryMinus.get()){
                    readResult.setWithUnaryMinus(true);
                    unaryMinus.set(false);
                }
                readResults.push(readResult);
                wasPreviousAnOperator.set(true);
            }
            case OPENING_BRACKET -> {
                if (unaryMinus.get()){
                    readResult.setWithUnaryMinus(true);
                    unaryMinus.set(false);
                }
                readResults.push(readResult);
                wasPreviousAnOperator.set(true);
            }
            case CLOSING_BRACKET -> {
                handleClosingBracket(readResults,result);
                wasPreviousAnOperator.set(false);
            }
            case COMMA -> {
                handleComma(readResults,result);
                wasPreviousAnOperator.set(true);
            }
        }
    }

    private static void handleComma(Stack<ReadResult> readResults,List<SYNode> resultList){
        while (!readResults.isEmpty()){
            ReadResult result = readResults.peek();
            if (result.type == TokenType.FUNCTION){
                break;
            }else{
                readResults.pop();
                resultList.add(OPERATORS.get(result.str));
            }
        }
    }

    private static void handleClosingBracket(Stack<ReadResult> readResults,List<SYNode> resultList){
        while (!readResults.isEmpty()){
            ReadResult result = readResults.pop();
            if (result.type == TokenType.FUNCTION){
                SYFunction function = FunctionRegistry.getFunction(result.str);
                if (function == null){
                    throw new RuntimeException("Unknown function: " + result.str);
                }
                resultList.add(function);
                if (result.isWithUnaryMinus()){
                    resultList.add(new SubtractionFunction());
                }
                break;
            }else if (result.type == TokenType.OPENING_BRACKET){
                if (result.isWithUnaryMinus()){
                    resultList.add(new SubtractionFunction());
                }
                break;
            }else{
                resultList.add(OPERATORS.get(result.str));
            }
        }
    }



    private static void handleOperatorToken(ReadResult operator,Stack<ReadResult> readResults,List<SYNode> resultList){
        if (!readResults.isEmpty()) {
            int priority = OPERATOR_PRIORITY.get(operator.str);
            while (true){
                if (readResults.isEmpty()){
                    readResults.push(operator);
                    break;
                }
                ReadResult result = readResults.peek();
                if (result.type == TokenType.OPERATOR){
                    int priority2 = OPERATOR_PRIORITY.get(result.str);
                    if (priority <= priority2){
                        readResults.pop();
                        resultList.add(OPERATORS.get(result.str));
                    }else{
                        readResults.push(operator);
                        break;
                    }
                }else{
                    readResults.push(operator);
                    break;
                }
            }

        }else{
            readResults.push(operator);
        }
    }






    public static ReadResult next(StringBuilder string){
        if (string.isEmpty()) return null;
        char c = string.charAt(0);
        if (OPERATORS.containsKey(c+"")){
            string.delete(0,1);
            return new ReadResult(c + "",TokenType.OPERATOR);
        }else if (Character.isLetter(c)){
            ReadResult v = readFunctionOrVariable(string);
            string.delete(0,v.type == TokenType.VARIABLE ? v.str.length() : v.str.length() + 1);
            return v;
        }else if (c == '('){
            string.delete(0,1);
            return new ReadResult("(",TokenType.OPENING_BRACKET);
        }else if (c == ')'){
            string.delete(0,1);
            return new ReadResult(")",TokenType.CLOSING_BRACKET);
        }else if (c == ','){
            string.delete(0,1);
            return new ReadResult(",",TokenType.COMMA);
        }else{
            String val = readNumber(string);
            string.delete(0,val.length());
            return new ReadResult(val,TokenType.NUMBER);
        }
    }



    private static ReadResult readFunctionOrVariable(StringBuilder builder){
        char c;
        StringBuilder val = new StringBuilder();
        int i = 0;
        while (i < builder.length() && (Character.isLetter(c = builder.charAt(i)) || c == '.' || c == '_' || Character.isDigit(c))){
            val.append(c);
            i++;
        }
        if (i < builder.length() && builder.charAt(i) == '('){
            return new ReadResult(val.toString(),TokenType.FUNCTION);
        }else{
            return new ReadResult(val.toString(),TokenType.VARIABLE);
        }
    }

    private static String readNumber(StringBuilder builder){
        char c;
        StringBuilder val = new StringBuilder();
        int i = 0;
        while (i < builder.length() && (Character.isDigit(c = builder.charAt(i++)) || c == '.')){
            val.append(c);
        }
        return val.toString();
    }

    public enum TokenType{
        OPERATOR,
        CLOSING_BRACKET,
        OPENING_BRACKET,
        NUMBER,
        FUNCTION,
        VARIABLE,
        COMMA


    }

    public static class ReadResult {

        private String str;
        private TokenType type;
        private boolean isWithUnaryMinus;


        public ReadResult(String str,TokenType type){
            this.str = str;
            this.type = type;
        }

        public void setWithUnaryMinus(boolean withUnaryMinus) {
            isWithUnaryMinus = withUnaryMinus;
        }

        public boolean isWithUnaryMinus() {
            return isWithUnaryMinus;
        }
    }



}
