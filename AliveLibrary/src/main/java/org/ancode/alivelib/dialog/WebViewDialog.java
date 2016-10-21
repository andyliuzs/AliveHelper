package org.ancode.alivelib.dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.ancode.alivelib.R;
import org.ancode.alivelib.config.HelperConfig;
import org.ancode.alivelib.utils.Log;
import org.ancode.alivelib.utils.UiHelper;


public class WebViewDialog extends Dialog {
    ReceiverHomeAndMenu receiverHomeAndMenu;

    public WebViewDialog(Context context) {
        super(context);
    }

    public WebViewDialog(Context context, int theme) {
        super(context, theme);
        receiverHomeAndMenu = new ReceiverHomeAndMenu();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        HelperConfig.CONTEXT.registerReceiver(receiverHomeAndMenu, intentFilter);
    }


    public static class Builder {
        private Context context;
        private String title;
        private String positiveButtonText;
        private String negativeButtonText;
        private int positiveButtonTextColor = -1;
        private int negativeButtonTextColor = -1;
        private String url;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private boolean showProgress = true;
        WebView webView = null;

        public Builder(Context context) {
            this.context = context;
        }


        public Builder showProgress(boolean b) {
            this.showProgress = b;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }


        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }


        public Builder setPositiveButtonTextColor(int color) {
            this.positiveButtonTextColor = color;
            return this;
        }

        public Builder setNegativeButtonTextColor(int color) {
            this.negativeButtonTextColor = color;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public WebViewDialog create() {

            LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final WebViewDialog dialog = new WebViewDialog(context, R.style.AliveDialog);
            View layout = inflater.inflate(R.layout.alive_webview_dialog_layout, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView _title = (TextView) layout.findViewById(R.id.title);
            if (TextUtils.isEmpty(title)) {
                View title_bar = layout.findViewById(R.id.rl_dialog_single_title);
                title_bar.setVisibility(View.GONE);
            } else {
                _title.setText(title);
            }
            // set the confirm button
            if (positiveButtonText != null) {
                TextView _positiveButton = (TextView) layout.findViewById(R.id.positiveButton);
                _positiveButton.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    _positiveButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
                int color = _positiveButton.getTextColors().getDefaultColor();
                if (positiveButtonTextColor != -1) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        color = context.getColor(positiveButtonTextColor);
                    } else {
                        color = context.getResources().getColor(positiveButtonTextColor);
                    }
                } else {
                    color = UiHelper.getThemeColor();
                }
                _positiveButton.setTextColor(color);

            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                TextView _negativeButton = (TextView) layout.findViewById(R.id.negativeButton);
                _negativeButton.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    _negativeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }

                int color = _negativeButton.getTextColors().getDefaultColor();
                if (negativeButtonTextColor != -1) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        color = context.getColor(negativeButtonTextColor);
                    } else {
                        color = context.getResources().getColor(negativeButtonTextColor);
                    }

                } else {
                    color = UiHelper.getThemeColor();
                }
                _negativeButton.setTextColor(color);
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }

            if ((negativeButtonText == null && positiveButtonText != null) || (negativeButtonText != null && positiveButtonText == null)) {
                layout.findViewById(R.id.dialog_single_line).setVisibility(View.GONE);
            }

            if (negativeButtonText == null && positiveButtonText == null) {
                layout.findViewById(R.id.ll_dialog_single_btn).setVisibility(View.GONE);
                layout.findViewById(R.id.line_up).setVisibility(View.GONE);
            }

            webView = (WebView) layout.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            //获取WebSettings对象,设置缩放
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setDomStorageEnabled(true);//允许DCOM

            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                    if (url.startsWith("http:") || url.startsWith("https:")) {
//                        return false;
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        view.loadUrl(request.getUrl().toString());
//                    }
//                    return true;
//                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (URLUtil.isNetworkUrl(url)) {
                        return false;
                    }
                    Log.v("WebViewDialog", "start Web url is = " + url);
//                    context.startActivity(IntentUtils.getNormalWeb(url));
                    try {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse(url));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            ProgressBar progressBar = null;
            if (showProgress) {
                progressBar = (ProgressBar) layout.findViewById(R.id.progress);
                final ProgressBar finalProgressBar = progressBar;
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        finalProgressBar.setProgress(newProgress);
                        if (newProgress == 100) {
                            finalProgressBar.setVisibility(View.INVISIBLE);
                        } else {
                            finalProgressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            dialog.setContentView(layout);
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (webView != null) {
                        webView.destroy();
                        webView = null;
                    }
                }
            });
            dialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (webView != null) {
                        webView.destroy();
                        webView = null;
                    }
                }
            });
            return dialog;
        }

    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            HelperConfig.CONTEXT.unregisterReceiver(receiverHomeAndMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ReceiverHomeAndMenu extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                cancel();
                dismiss();
            }
        }
    }

}
