package com.utn.tp.prog3.ui.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) //Ignora campos extra del JSON (como 'pageable', 'sort', etc.)
@Getter
@Setter
public class PageResponse<T> {

    //PageResponse permitirá indicarle a jackson como debe deserializar la información que recibe de los controladores, ya que
    //el formato de respuesta de Spring Data es un poco complejo y no se puede mapear directamente a una clase genérica sin indicarle a jackson como hacerlo.

    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number;        //número de página actual (0-indexed)
    private int size;          //tamaño de la página
    private boolean last;
    private boolean first;
    private int numberOfElements;

    //Constructor vacío (obligatorio para Jackson)
    public PageResponse() {
        this.content = Collections.emptyList();
    }


}