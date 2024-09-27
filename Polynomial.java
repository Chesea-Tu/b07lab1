import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Polynomial {
    private double[] coefficients;
    private int[] exponents;

    public Polynomial() {
        this.coefficients = null;
        this.exponents = null;
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients;
        this.exponents = exponents;
    }
    
    public Polynomial(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            parsePolynomial(line); 
        }

        scanner.close();
    }
    
    private void parsePolynomial(String line) {
    	String[] terms = line.split("(?=\\+)|(?=-)");
    	ArrayList<Double> coefficients = new ArrayList<>();
        ArrayList<Integer> exponents = new ArrayList<>();
    	for (String term : terms) {
    		double coefficient;
    		int exponent;
    		int xindex = term.indexOf('x');
    		if(xindex == -1) {
    			coefficient = Double.parseDouble(term);
                exponent = 0;
    		}
    		else {
    			String coefficientPart = term.substring(0, xindex);
    			if (coefficientPart.isEmpty() || coefficientPart.equals("+")) {
                    coefficient = 1; 
    			}
    			else if (coefficientPart.equals("-")) {
    				coefficient = -1;
    			}
    			else {
    				coefficient = Double.parseDouble(coefficientPart);
    			}
    			
    			String exponentPart = term.substring(xindex + 1);
    			if (exponentPart.isEmpty()) {
    				exponent = 1;
    			}
    			else {
    				exponent = Integer.parseInt(exponentPart);
    			}
    		}
    		coefficients.add(coefficient);
            exponents.add(exponent);
    	}   
    }
    
    public void saveToFile(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            String polynomialString = this.toString();

            writer.write(polynomialString);

            writer.close();
            System.out.println("Polynomial saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the polynomial to the file.");
            e.printStackTrace();
        }
    }
    
    public HashMap<Integer, Double> mapping() {
        HashMap<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < this.exponents.length; i++) {
            map.put(this.exponents[i], this.coefficients[i]);
        }
        return map;
    }
    
    public Polynomial add(Polynomial that) {
    	HashMap<Integer, Double> map1 = this.mapping();
    	HashMap<Integer, Double> map2 = that.mapping();
    	HashMap<Integer, Double> resultMap = new HashMap<>(map1);
    	for (Integer exponent : map2.keySet()) {
            if (resultMap.containsKey(exponent)) {
                resultMap.put(exponent, resultMap.get(exponent) + map2.get(exponent));
            } else {
            	resultMap.put(exponent, map2.get(exponent));
            }
    	}
    	int size = resultMap.size();
    	double[] resultCoefficients = new double[size];
    	int[] resultExponents = new int[size];
    	int i = 0;
    	for (Integer exponent : resultMap.keySet()) {
    		resultCoefficients[i] = resultMap.get(exponent);
            resultExponents[i] = exponent;
            i++;
         }
        return new Polynomial(resultCoefficients, resultExponents);
    }
    
    public Polynomial multiply(Polynomial that) {
    	HashMap<Integer, Double> resultMap = new HashMap<>();
        for (int i = 0; i < this.exponents.length; i++) {
            for (int j = 0; j < that.exponents.length; j++) {
                int newExponent = this.exponents[i] + that.exponents[j]; 
                double newCoefficient = this.coefficients[i] * that.coefficients[j]; 
                if(resultMap.containsKey(newExponent)) {
                	resultMap.put(newExponent, resultMap.get(newExponent) + newCoefficient);
                }
                else {
                	resultMap.put(newExponent, newCoefficient);
                }
            }
        }
        int size = resultMap.size();
        double[] resultCoefficients = new double[size];
        int[] resultExponents = new int[size];
        int index = 0;

        for (HashMap.Entry<Integer, Double> entry : resultMap.entrySet()) {
            resultExponents[index] = entry.getKey();
            resultCoefficients[index] = entry.getValue();
            index++;
        }
        return new Polynomial(resultCoefficients, resultExponents);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            double coefficient = coefficients[i];
            int exponent = exponents[i];

            if (coefficient == 0) {
                continue;
            }
            if (i > 0 && coefficient > 0) {
                sb.append(" + ");
            } else if (coefficient < 0) {
                sb.append(" - ");
                coefficient = -coefficient;
            }

            if (coefficient != 1 || exponent == 0) {
                sb.append(coefficient);
            }
            if (exponent > 0) {
                sb.append("x");
                if (exponent > 1) {
                    sb.append("^").append(exponent);
                }
            }
        }

        return sb.toString();
    }


    public double evaluate(double x) {
        double result = 0;
        if(this.coefficients == null) return result;
        for (int i = 0; i < this.coefficients.length; i++) {
            result += this.coefficients[i] * Math.pow(x,this.exponents[i]);
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}
