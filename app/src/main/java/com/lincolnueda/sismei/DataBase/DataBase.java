package com.lincolnueda.sismei.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lincoln Ueda on 17/08/2015.
 */
public class DataBase extends SQLiteOpenHelper {


    public DataBase(Context context){
        super(context,"SisMEIDB",null,5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptSql.getCreateUsuario());
        db.execSQL(ScriptSql.getCreateFuncionario());
        db.execSQL(ScriptSql.getCreateClienteFornecedor());
        db.execSQL(ScriptSql.getCreateEndereco());
        db.execSQL(ScriptSql.getCreateTelefone());
        db.execSQL(ScriptSql.getCreateEmail());
        db.execSQL(ScriptSql.getCreateFinanceiro());
        db.execSQL(ScriptSql.getCreateEtapaProducao());
        db.execSQL(ScriptSql.getCreatePedido());
        db.execSQL(ScriptSql.getCreateProduto());
        db.execSQL(ScriptSql.getCreateMaterial());
        db.execSQL(ScriptSql.getCreateListaMaterial());
        db.execSQL(ScriptSql.getCreateOrcamento());
        db.execSQL(ScriptSql.getCreateMaterialOrcamento());
        db.execSQL(ScriptSql.getCreateListaPedido());
        db.execSQL(ScriptSql.getCreateEmpresa());


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
