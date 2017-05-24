package edu.itchii.plat2.pf;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;



 class Encuesta extends Activity {

    CheckBox ch1, ch2, ch3, ch4;
    Button salvar, guardar;
    int checked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeDB();

        salvar = (Button)findViewById(R.id.salvar);
        guardar = (Button)findViewById(R.id.guardar);


        ch1 = (CheckBox)findViewById(R.id.ch1);
        ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked = 1;
                }else{
                    checked = 0;
                }
            }
        });

        ch2 = (CheckBox)findViewById(R.id.ch2);
        ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked = 1;
                }else{
                    checked = 0;
                }
            }
        });

        ch3 = (CheckBox)findViewById(R.id.ch3);
        ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked = 1;
                }else{
                    checked = 0;
                }
            }
        });

        ch4 = (CheckBox)findViewById(R.id.ch4);
        ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked = 1;
                }else{
                    checked = 0;
                }
            }
        });



        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTable(checked);
                Toast.makeText(getApplicationContext(), "Saved '"+ checked + "' in DB", Toast.LENGTH_SHORT).show();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("getData(1) : " + getData(1));
                Toast.makeText(getApplicationContext(), "  " + getData(1), Toast.LENGTH_SHORT).show();
                if(getData(1).equalsIgnoreCase("0"))
                    ch1.setChecked(false);
                else
                    ch1.setChecked(true);
            }
        });
    }



    public  void initializeDB(){
        EncDB user = new EncDB();
        String[] tableCreateArray = { user.getDatabaseCreateQuery() };
        dbOperation operation = new dbOperation(this,tableCreateArray);
        operation.open();
        operation.close();
    }


    public  void saveData(int data){
        dbOperation operationObj = new dbOperation(this);
        operationObj.open();
        EncDB Fields = new EncDB();
        ContentValues initialValues = new ContentValues();
        initialValues.put(Fields.getScore(), data);
        operationObj.insertTableData(Fields.getTABLE_NAME(),initialValues);
        operationObj.close();
    }


    public String getData(int id){
        String _data = "";
        dbOperation operationObj = new dbOperation(this);
        operationObj.open();
        EncDB fields = new EncDB();
        String  condition2 = fields.getID() + " ='" + id + "'";
        String[] dbFields4 = {fields.getScore()};
        Cursor cursor2 =  operationObj.getTableRow(fields.getTABLE_NAME(),dbFields4,condition2,fields.getID() + " ASC ","1");
        if(cursor2.getCount() > 0)
        {
            cursor2.moveToFirst();
            do{
                _data = cursor2.getString(0);
            }while(cursor2.moveToNext());
        }else{
            _data = "error";
        }
        cursor2.close();
        cursor2.deactivate();
        operationObj.close();
        return _data;
    }


    public void updateTable(int updt_data){
        dbOperation operationObj = new dbOperation(this);
        operationObj.open();
        EncDB Fields = new EncDB();

        String file_ = getData(1);
        if(file_.equals("error")){
            saveData(updt_data);
        }else{
            String  condition = Fields.getID() +" = '1'";
            ContentValues initialValues = new ContentValues();
            initialValues.put(Fields.getScore(), updt_data);
            operationObj.updateTable(Fields.getTABLE_NAME(),initialValues,condition);
        }
        operationObj.close();
    }
}


