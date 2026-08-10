package entity;

public class SimilarStocks {

    final private SimilarStockInfo[] similarStocks;

    public SimilarStocks(int numResults) {
        this.similarStocks = new SimilarStockInfo[numResults];
    }

    public String getSymbol(int i) {
        return this.similarStocks[i].getSymbol();
    }

    public String getName(int i) {
        return this.similarStocks[i].getName();
    }

    public String getRegion(int i) {
        return this.similarStocks[i].getRegion();
    }

    public void setSimilarStock(int i, String symbol, String name, String region) {
        this.similarStocks[i] = new SimilarStockInfo(symbol, name, region);
    }

    public int getLength(){
        return this.similarStocks.length;
    }

    private static class SimilarStockInfo {
        final private String symbol;
        final private String name;
        final private String region;

        public SimilarStockInfo(String symbol, String name, String region) {
            this.symbol = symbol;
            this.name = name;
            this.region = region;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }
    }
}
