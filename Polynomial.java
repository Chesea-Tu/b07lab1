public class Polynomial {
    private double[] coefficients;

    public Polynomial() {
        this.coefficients = new double[] {0};
    }

    public Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public Polynomial add(Polynomial that) {
        int maxLength = Math.max(this.coefficients.length, that.coefficients.length);
        double[] result = new double[maxLength];

        for (int i = 0; i < maxLength; i++) {
            double thisCo;
            if (i < this.coefficients.length) {
                thisCo = this.coefficients[i];
            } else {
                thisCo = 0;
            }

            double thatCo;
            if (i < that.coefficients.length) {
                thatCo = that.coefficients[i];
            } else {
                thatCo = 0;
            }

            result[i] = thisCo + thatCo;
        }
        return new Polynomial(result);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < this.coefficients.length; i++) {
            result += this.coefficients[i] * Math.pow(x, i);
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}
