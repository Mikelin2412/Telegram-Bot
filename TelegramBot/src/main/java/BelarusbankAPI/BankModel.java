package BelarusbankAPI;

public class BankModel {
    private Double usd_in;
    private Double usd_out;
    private Double eur_in;
    private Double eur_out;
    private Double rub_in;
    private Double rub_out;
    private String city_name;

    public Double getUsd_in() {
        return usd_in;
    }

    public void setUsd_in(Double usd_in) {
        this.usd_in = usd_in;
    }

    public Double getUsd_out() {
        return usd_out;
    }

    public void setUsd_out(Double usd_out) {
        this.usd_out = usd_out;
    }

    public Double getEur_in() {
        return eur_in;
    }

    public void setEur_in(Double eur_in) {
        this.eur_in = eur_in;
    }

    public Double getEur_out() {
        return eur_out;
    }

    public void setEur_out(Double eur_out) {
        this.eur_out = eur_out;
    }

    public Double getRub_in() {
        return rub_in;
    }

    public void setRub_in(Double rub_in) {
        this.rub_in = rub_in;
    }

    public Double getRub_out() {
        return rub_out;
    }

    public void setRub_out(Double rub_out) {
        this.rub_out = rub_out;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
