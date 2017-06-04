package br.com.lvbfontes.piimobiliaria.Modelo;

/**
 * Created by Lucas on 03/06/2017.
 */

public class Imovel {

    private String Comodos;
    private String Contrato;
    private String Imagem;
    private String Tipo;
    private String Area;
    private String Valor;

    public Imovel() {

    }

    public Imovel(String comodos, String contrato, String imagem, String tipo, String area, String valor) {
        Comodos = comodos;
        Contrato = contrato;
        Imagem = imagem;
        Tipo = tipo;
        Area = area;
        Valor = valor;
    }

    public String getComodos() {
        return Comodos;
    }

    public void setComodos(String comodos) {
        Comodos = comodos;
    }

    public String getContrato() {
        return Contrato;
    }

    public void setContrato(String contrato) {
        Contrato = contrato;
    }

    public String getImagem() {
        return Imagem;
    }

    public void setImagem(String imagem) {
        Imagem = imagem;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }
}
