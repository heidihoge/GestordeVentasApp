package com.inge2.gestorventas.gestordeventas;

/**
 * Created by ADMIN on 15/10/2016.
 */

public class DataBaseManager {
    //nombre de la tabla
    public static final String TABLE_NAME = "clientes";

    //nombre de los campos
    public static final String CL_ID = "id";
    public static final String CL_NAME = "nombre";
    public static final String CL_PHONE = "telefono";
    public static final String CL_DIR = "direccion";

    //sentencias SQL .. como es en java

    public static final String CREATE_TABLE = "create table " +TABLE_NAME+ " ("
            + CL_ID + " integer primary key autoincrement,"
            + CL_NAME + " text not null,"
            + CL_PHONE + " text,"
            + CL_DIR + " text);";





}
