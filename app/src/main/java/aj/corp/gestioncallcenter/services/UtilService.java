package aj.corp.gestioncallcenter.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import aj.corp.gestioncallcenter.shared.ApplicationContext;

import java.io.UnsupportedEncodingException;

public class UtilService {

    private final Context context = ApplicationContext.getAppContext();

    public SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences("preferences",Context.MODE_PRIVATE);
    }

    public void saveStringToSharedPreferences(String key, String value){
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putString(key,value);
        editor.commit();
    }

    public void saveIntToSharedPreferences(String key, int value){
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public void saveBooleanToSharedPreferences(String key, boolean value){
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public String getStringFromSharedPreferences(String key){
        return this.getSharedPreferences().getString(key,null);
    }

    public int getIntFromSharedPreferences(String key){
        return this.getSharedPreferences().getInt(key, -1);
    }

    public boolean getBooleanFromSharedPreferences(String key){
        return this.getSharedPreferences().getBoolean(key, false);
    }

    public void deleteKeyFromSharedPreferences(String key){
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearSharedPreferences(){
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.clear();
        editor.commit();
    }



    public void saveTokenToSharedPreferences(String token){
        this.saveStringToSharedPreferences("token", token);
    }

    public String getTokenFromSharedPreferences(){
        return this.getStringFromSharedPreferences("token");
    }

    public void deleteTokenFromSharedPreferences(){
        this.deleteKeyFromSharedPreferences("token");
    }

    public void saveRefreshTokenToSharedPreferences(String refresh){
        this.saveStringToSharedPreferences("refresh_token",refresh);
    }

    public String getRefreshTokenFromoSharedPreferences(){
        return this.getStringFromSharedPreferences("refresh_token");
    }

    public void deleteRefreshTokenFromSharedPreferences(){
        this.deleteKeyFromSharedPreferences("refresh_token");
    }

    /*public void saveUserIdToSharedPreferences(String user_id){
        this.saveStringToSharedPreferences("Id", user_id);
    }

    public String getUserIdToSharedPreferences(){
        return this.getStringFromSharedPreferences("Id");
    }

    public void deleteUserIdToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("Id");
    }

    public void saveUserToSharedPreferences(String user){
        this.saveStringToSharedPreferences("User", user);
    }

    public String getUserToSharedPreferences(){
        return this.getStringFromSharedPreferences("User");
    }

    public void deleteUserToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("User");
    }

    public void saveNombreToSharedPreferences(String nombre){
        this.saveStringToSharedPreferences("Nombre", nombre);
    }

    public String getNombreToSharedPreferences(){
        return this.getStringFromSharedPreferences("Nombre");
    }

    public void deleteNombreToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("Nombre");
    }

    public void saveApellidosToSharedPreferences(String apellidos){
        this.saveStringToSharedPreferences("Apellidos", apellidos);
    }

    public String getApellidosToSharedPreferences(){
        return this.getStringFromSharedPreferences("Apellidos");
    }

    public void deleteApellidosToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("Apellidos");
    }

    public void saveDireccionToSharedPreferences(String direccion){
        this.saveStringToSharedPreferences("Direccion", direccion);
    }

    public String getDireccionToSharedPreferences(){
        return this.getStringFromSharedPreferences("Direccion");
    }

    public void deleteDireccionToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("Direccion");
    }

    public void saveTelefonoToSharedPreferences(String telefono){
        this.saveStringToSharedPreferences("Telefono", telefono);
    }

    public String getTelefonoToSharedPreferences(){
        return this.getStringFromSharedPreferences("Telefono");
    }

    public void deleteTelefonoToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("Telefono");
    }

    public void saveFechaContToSharedPreferences(String fecha_cont){
        this.saveStringToSharedPreferences("Fecha_contratacion", fecha_cont);
    }

    public String getFechaContToSharedPreferences(){
        return this.getStringFromSharedPreferences("Fecha_contratacion");
    }

    public void deleteFechaContToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("Fecha_contratacion");
    }

    public void savePermisosToSharedPreferences(String permisos){
        this.saveStringToSharedPreferences("permisos", permisos);
    }

    public String getPermisosToSharedPreferences(){
        return this.getStringFromSharedPreferences("permisos");
    }

    public void deletePermisosToSharedPreferences(){
        this.deleteKeyFromSharedPreferences("permisos");
    }

    public void saveAllSharedPreferences(String token, String refresh, String user_id, String user, String nombre, String apellidos, String direccion, String telefono, String fecha_cont, String permisos){
        saveTokenToSharedPreferences(token);
        saveRefreshTokenToSharedPreferences(refresh);
        saveUserIdToSharedPreferences(user_id);
        saveUserToSharedPreferences(user);
        saveNombreToSharedPreferences(nombre);
        saveApellidosToSharedPreferences(apellidos);
        saveDireccionToSharedPreferences(direccion);
        saveTelefonoToSharedPreferences(telefono);
        saveFechaContToSharedPreferences(fecha_cont);
        savePermisosToSharedPreferences(permisos);
    }*/

    public String encodeBase64(String str){
        try {
            byte[] bytes = str.getBytes("UTF-8");
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }


    }
    public String decodeBase64(String str){
        try{
            byte[] data = Base64.decode(str, Base64.DEFAULT);
            return new String(data, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
