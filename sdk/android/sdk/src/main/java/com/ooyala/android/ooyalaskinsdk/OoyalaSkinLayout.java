package com.ooyala.android.ooyalaskinsdk;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.facebook.internal.BundleJSONConverter;
import com.facebook.react.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.ooyala.android.OoyalaPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zchen on 9/21/15.
 */
public class OoyalaSkinLayout extends FrameLayout {
  private FrameLayout _playerFrame;
  private OoyalaPlayer _player;
  private ReactInstanceManager _reactInstanceManager;
  private ReactRootView _rootView;
  private int viewWidth,viewHeight;


  /**
   * Initialize the OoyalaPlayerLayout with the given Context
   * @param context the Context to use
   */
  public OoyalaSkinLayout(Context context) {
    super(context);
  }

  /**
   * Initialize the OoyalaPlayerLayout with the given Context and AttributeSet
   * @param context the Context to use
   * @param attrs the AttributeSet to use
   */
  public OoyalaSkinLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Initialize the OoyalaPlayerLayout with the given Context, AttributeSet, and style
   * @param context the Context to use
   * @param attrs the AttributeSet to use
   * @param defStyle the style of the Layout
   */
  public OoyalaSkinLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  @Override
  protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
      super.onSizeChanged(xNew, yNew, xOld, yOld);
      viewWidth = xNew;
      viewHeight = yNew;
  }

  public int getViewHeight() {
    return viewHeight;
  }

  public int getViewWidth() {
    return viewWidth;
  }

  public void setupViews(Application app, OoyalaPlayer p) {
    FrameLayout.LayoutParams frameLP =
        new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);
    _playerFrame = new FrameLayout(getContext());
    this.addView(_playerFrame, frameLP);

    JSONObject configJson = loadInitialProperties();
    Bundle launchOptions = null; //Initial properties.
    if (configJson != null) {
      try {
        launchOptions = BundleJSONConverter.convertToBundle(configJson);
      } catch (JSONException e) {
        e.printStackTrace();
        launchOptions = null;
      }
    }

    _rootView = new ReactRootView(getContext());
    _reactInstanceManager = ReactInstanceManager.builder()
        .setApplication(app)
        .setBundleAssetName("index.android.bundle")
        .setJSMainModuleName("index.android")
        .addPackage(new OoyalaReactPackage(this, p))
        .setUseDeveloperSupport(BuildConfig.DEBUG)
        .setInitialLifecycleState(LifecycleState.RESUMED)

        .build();
    _rootView.startReactApplication(_reactInstanceManager, "OoyalaSkin", launchOptions);
    this.addView(_rootView, frameLP);
  }

  public FrameLayout getPlayerLayout() {
    return _playerFrame;
  }

  private JSONObject loadInitialProperties() {
    String json = null;
    try {

      InputStream is = getContext().getAssets().open("skin.json");
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();

      json = new String(buffer, "UTF-8");

    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(json);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return jsonObject;
  }
}