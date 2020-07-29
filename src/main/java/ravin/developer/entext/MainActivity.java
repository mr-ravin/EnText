package ravin.developer.entext;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    private static final int pswdIterations = 1000;
    private static final int keySize = 128;
    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    private static final String secretKeyInstance = "PBKDF2WithHmacSHA1";
    private  String AESSalt = "AES128bit";
    private  String initializationVector = "2195081919109305";

    private static byte[] getRaw(String plainText, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), pswdIterations, keySize);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }



    public String encryptaes(String text_key, String text_message) throws Exception {
        SecretKeySpec sks = new SecretKeySpec(getRaw(text_key, AESSalt), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(initializationVector.getBytes()));
        byte[] encoded_bytes=cipher.doFinal(text_message.getBytes("UTF-8"));
        String cipherText = Base64.encodeToString (encoded_bytes, Base64.DEFAULT);
        return cipherText;
    }

    public String decryptaes(String text_key, String text_message) throws Exception {
        SecretKeySpec sks = new SecretKeySpec(getRaw(text_key, AESSalt), "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(initializationVector.getBytes()));
        byte[] decoded_bytes= cipher.doFinal(Base64.decode(text_message,Base64.DEFAULT));
        String decipherText= new String(decoded_bytes,"UTF-8");
        return decipherText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button encrypt_button=(Button)findViewById(R.id.button_encrypt);
        Button decrypt_button=(Button)findViewById(R.id.button_decrypt);
        Button about_us=(Button)findViewById(R.id.button_aboutus);
        encrypt_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                EditText edkey = (EditText) findViewById(R.id.text_key);
                EditText edmsg = (EditText) findViewById(R.id.text_message);
                EditText edaessalt = (EditText) findViewById(R.id.text_aessalt);
                EditText ediv = (EditText) findViewById(R.id.text_iv);
                AESSalt = edaessalt.getText().toString();
                initializationVector = ediv.getText().toString();
                String text_key= edkey.getText().toString();
                String text_message= edmsg.getText().toString();
                String cipherText = "No data was entered !!!";
                try {
                    cipherText = encryptaes(text_key, text_message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edmsg.setText(cipherText);
            }
        });
        decrypt_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText edkey = (EditText) findViewById(R.id.text_key);
                EditText edmsg = (EditText) findViewById(R.id.text_message);
                EditText edaessalt = (EditText) findViewById(R.id.text_aessalt);
                EditText ediv = (EditText) findViewById(R.id.text_iv);
                AESSalt = edaessalt.getText().toString();
                initializationVector = ediv.getText().toString();
                String text_key= edkey.getText().toString();
                String text_message= edmsg.getText().toString();
                String decipherText = "No encrypted data was entered !!!";
                try {
                    decipherText = decryptaes(text_key,text_message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                edmsg.setText(decipherText);
            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(i);
            }
        });
    }
}