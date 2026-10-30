package file.encryption.decryption;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.FileEncryptor.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends Activity {
	
	public final int REQ_CD_FILE = 101;
	
	private String vInput = "";
	private String vOutput = "";
	private String vKey = "";
	
	private LinearLayout linear1;
	private ScrollView vscroll;
	private LinearLayout linear2;
	private LinearLayout linear_input;
	private LinearLayout linear_output;
	private HorizontalScrollView hscroll_key;
	private Button button_encrypt;
	private Button button_decrypt;
	private LinearLayout linear_debug;
	private EditText edittext_input;
	private ImageView imageview2;
	private EditText edittext_output;
	private LinearLayout linear_key;
	private EditText edittext_key;
	private TextView text_debug;
	
	private Intent file = new Intent(Intent.ACTION_GET_CONTENT);
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			} else {
				initializeLogic();
			}
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		vscroll = findViewById(R.id.vscroll);
		linear2 = findViewById(R.id.linear2);
		linear_input = findViewById(R.id.linear_input);
		linear_output = findViewById(R.id.linear_output);
		hscroll_key = findViewById(R.id.hscroll_key);
		button_encrypt = findViewById(R.id.button_encrypt);
		button_decrypt = findViewById(R.id.button_decrypt);
		linear_debug = findViewById(R.id.linear_debug);
		edittext_input = findViewById(R.id.edittext_input);
		imageview2 = findViewById(R.id.imageview2);
		edittext_output = findViewById(R.id.edittext_output);
		linear_key = findViewById(R.id.linear_key);
		edittext_key = findViewById(R.id.edittext_key);
		text_debug = findViewById(R.id.text_debug);
		file.setType("*/*");
		file.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		linear_input.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		button_encrypt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Task("e");
			}
		});
		
		button_decrypt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_Task("d");
			}
		});
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(file, REQ_CD_FILE);
			}
		});
		
		edittext_key.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				if (_charSeq.length() < 4) {
					((EditText)edittext_key).setError("Password must be at least 4 characters");
				} else {
					
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
	}
	
	private void initializeLogic() {
		if (!FileUtil.isDirectory("/storage/emulated/0/File Encryptor ")) {
			FileUtil.makeDir("/storage/emulated/0/File Encryptor ");
		}
		edittext_output.setText("/storage/emulated/0/File Encryptor");
		linear_input.setElevation(6);
		linear_output.setElevation(6);
		linear_debug.setElevation(6);
		linear_key.setElevation(6);
		vscroll.setFillViewport(true);
		hscroll_key.setFillViewport(true);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_FILE:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				edittext_input.setText(_filePath.get((int)(0)));
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	public void _Task(final String _type) {
		vInput = edittext_input.getText().toString();
		vOutput = edittext_output.getText().toString();
		vKey = edittext_key.getText().toString();
		if (vInput.equals("")) {
			text_debug.setText("Enter input");
		} else {
			if (vOutput.equals("")) {
				text_debug.setText("Enter output");
			} else {
				if (vKey.equals("")) {
					text_debug.setText("Enter key");
				} else {
					if (!FileUtil.isExistFile(vInput)) {
						text_debug.setText("Input not exists");
					} else {
						text_debug.setText("Please wait...");
						button_encrypt.setEnabled(false);
						button_decrypt.setEnabled(false);
						edittext_input.setEnabled(false);
						edittext_output.setEnabled(false);
						edittext_key.setEnabled(false);
						if (_type.equals("e")) {
							FileCryptoTask.encryptAsync(vInput, vOutput.concat("/".concat(Uri.parse(vInput).getLastPathSegment().concat(".enc"))),vKey, new FileEncryptor.EncryptProgressCallback() {
								
								@Override
								public void onProgress( final long _bytesTransferred,final long _totalBytes,final int _percentage) {
									text_debug.setText(String.valueOf((long)((int)_bytesTransferred)).concat("/".concat(String.valueOf((long)((int)_totalBytes)).concat("\n Loading... ".concat(String.valueOf((long)(_percentage)).concat("%"))))));
								}
								@Override
								public void onSuccess() {
									text_debug.setText("Success... \n ".concat(vOutput.concat("/".concat(Uri.parse(vInput).getLastPathSegment().concat(".enc")))));
								}
								
								@Override
								public void onError(final String _error) {
									text_debug.setText(_error);
								}
							});
							
						} else {
							if (vInput.endsWith(".enc")) {
								FileCryptoTask.decryptAsync(vInput, vOutput.concat("/".concat(Uri.parse(vInput).getLastPathSegment().replace(".enc", ""))),vKey, new FileEncryptor.DecryptProgressCallback() {
									
									@Override
									public void onProgress( final long _bytesTransferred,final long _totalBytes,final int _percentage) {
										text_debug.setText(String.valueOf((long)((int)_bytesTransferred)).concat("/".concat(String.valueOf((long)((int)_totalBytes)).concat("\n Loading... ".concat(String.valueOf((long)(_percentage)).concat("%"))))));
									}
									@Override
									public void onSuccess() {
										text_debug.setText(vOutput.concat("/".concat(Uri.parse(vInput).getLastPathSegment().concat(".enc"))));
									}
									
									@Override
									public void onError(final String _error) {
										text_debug.setText(_error);
									}
								});
								
							} else {
								SketchwareUtil.showMessage(getApplicationContext(), "Please select .enc file!");
								text_debug.setText("Please select .enc file");
							}
						}
					}
				}
			}
		}
		button_encrypt.setEnabled(true);
		button_decrypt.setEnabled(true);
		edittext_input.setEnabled(true);
		edittext_output.setEnabled(true);
		edittext_key.setEnabled(true);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}