package com.rabindra.linkpays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Button btnAirplane;
    private Button btnReload;
    private TextView tvStatus;
    private TextView tvIp;
    private ProgressBar progressBar;
    private PowerManager.WakeLock wakeLock;

    private static final String TARGET_URL = "https://linkpays.in/hMB0hZ";
    private static final Random RANDOM = new Random();

    // Curated Genuine Modern Android Chrome Mobile User-Agents (8 Flagship Devices)
    private static final String[] MOBILE_USER_AGENTS = new String[]{
        "Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.102 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 14; SM-S928B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.102 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 14; CPH2581) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.86 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 14; 23090RA98G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.102 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 14; SM-A556B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.86 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 14; V2319) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.102 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 14; RMX3840) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.86 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 14; motorola edge 50 pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.102 Mobile Safari/537.36"
    };

    // Early anti-ad shield injected immediately at page start
    private static final String SCRIPT_EARLY_SHIELD =
        "(function(){\n" +
        "    try {\n" +
        "        window.open = function() { return { closed: true, location: { href: 'about:blank' } }; };\n" +
        "        var _origReplace = window.location.replace.bind(window.location);\n" +
        "        window.location.replace = function(u) {\n" +
        "            if (!u) return;\n" +
        "            var s = (u + '').toLowerCase();\n" +
        "            if (s.indexOf('linkpays.in') !== -1 || s.indexOf('roadtaxcalculatorr.my') !== -1 || s.indexOf('cloudflare') !== -1 || s.indexOf('mediafire.com') !== -1) {\n" +
        "                _origReplace(u);\n" +
        "            }\n" +
        "        };\n" +
        "        window.blurred = false;\n" +
        "        window.onblur = null;\n" +
        "    } catch(e){}\n" +
        "})();";

    // Automation & Anti-Fingerprint Script for LinkPays & BhootBypass
    private static final String SCRIPT_LINKPAYS = 
        "(function(){\n" +
        "    if (window.__linkpays_active) return;\n" +
        "\n" +
        "    var curHost = (location.hostname || '').toLowerCase();\n" +
        "    var curPath = (location.pathname || '').toLowerCase();\n" +
        "    if (curHost.indexOf('cloudflare.com') !== -1 || curPath.indexOf('/cdn-cgi/') !== -1) {\n" +
        "        return;\n" +
        "    }\n" +
        "\n" +
        "    window.__linkpays_active = true;\n" +
        "\n" +
        "    // 1. Anti-Fingerprint: Hardware Concurrency, Memory & Touch\n" +
        "    try {\n" +
        "        Object.defineProperty(navigator, 'hardwareConcurrency', { get: function() { return 8; } });\n" +
        "        Object.defineProperty(navigator, 'deviceMemory', { get: function() { return 8; } });\n" +
        "        Object.defineProperty(navigator, 'maxTouchPoints', { get: function() { return 5; } });\n" +
        "    } catch(e){}\n" +
        "\n" +
        "    // 2. Anti-Fingerprint: WebGL Vendor & Renderer Spoofing (Adreno 750 / Qualcomm)\n" +
        "    try {\n" +
        "        var origGetParam = WebGLRenderingContext.prototype.getParameter;\n" +
        "        WebGLRenderingContext.prototype.getParameter = function(param) {\n" +
        "            if (param === 0x9245) return 'Qualcomm';\n" +
        "            if (param === 0x9246) return 'Adreno (TM) 750';\n" +
        "            return origGetParam.apply(this, arguments);\n" +
        "        };\n" +
        "        if (window.WebGL2RenderingContext) {\n" +
        "            var origGetParam2 = WebGL2RenderingContext.prototype.getParameter;\n" +
        "            WebGL2RenderingContext.prototype.getParameter = function(param) {\n" +
        "                if (param === 0x9245) return 'Qualcomm';\n" +
        "                if (param === 0x9246) return 'Adreno (TM) 750';\n" +
        "                return origGetParam2.apply(this, arguments);\n" +
        "            };\n" +
        "        }\n" +
        "    } catch(e){}\n" +
        "\n" +
        "    // 3. Anti-Fingerprint: Canvas Noise Injection\n" +
        "    try {\n" +
        "        var origToDataURL = HTMLCanvasElement.prototype.toDataURL;\n" +
        "        HTMLCanvasElement.prototype.toDataURL = function() {\n" +
        "            var ctx = this.getContext('2d');\n" +
        "            if (ctx) {\n" +
        "                try {\n" +
        "                    var imgData = ctx.getImageData(0, 0, Math.min(10, this.width), Math.min(10, this.height));\n" +
        "                    if (imgData && imgData.data.length > 3) {\n" +
        "                        imgData.data[0] = (imgData.data[0] ^ 1);\n" +
        "                        ctx.putImageData(imgData, 0, 0);\n" +
        "                    }\n" +
        "                } catch(e){}\n" +
        "            }\n" +
        "            return origToDataURL.apply(this, arguments);\n" +
        "        };\n" +
        "    } catch(e){}\n" +
        "\n" +
        "    function notify(status) {\n" +
        "        if (window.AndroidBridge && typeof window.AndroidBridge.onStatusUpdate === 'function') {\n" +
        "            window.AndroidBridge.onStatusUpdate(status);\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    function checkDestination() {\n" +
        "        var curHref = (location.href || '').toLowerCase();\n" +
        "        if (curHref.indexOf('mediafire.com') !== -1 || document.querySelector('a[href*=\"mediafire.com\"]')) {\n" +
        "            if (window.AndroidBridge && typeof window.AndroidBridge.onDestinationDetected === 'function') {\n" +
        "                window.AndroidBridge.onDestinationDetected(location.href);\n" +
        "                return true;\n" +
        "            }\n" +
        "        }\n" +
        "        return false;\n" +
        "    }\n" +
        "\n" +
        "    // 4. Inject dj's Cloudflare Worker script\n" +
        "    var workerInjected = false;\n" +
        "    function injectWorkerScript() {\n" +
        "        if (workerInjected) return;\n" +
        "        try {\n" +
        "            var s = document.createElement('script');\n" +
        "            s.src = 'https://bhootbypass.redbhoot505.workers.dev/?host=' + encodeURIComponent(location.hostname);\n" +
        "            (document.head || document.body || document.documentElement).appendChild(s);\n" +
        "            workerInjected = true;\n" +
        "            notify('Worker Injected: ' + location.hostname);\n" +
        "        } catch(e){}\n" +
        "    }\n" +
        "\n" +
        "    // 5. Native Fast Automation for LinkPays & Safelink/Arlink pages\n" +
        "    function runLinkPaysAutomation() {\n" +
        "        if (checkDestination()) return;\n" +
        "        injectWorkerScript();\n" +
        "\n" +
        "        try {\n" +
        "            // Step 1: linkpays.in gateway (#goBtn or proceed())\n" +
        "            if (curHost.indexOf('linkpays.in') !== -1) {\n" +
        "                var goBtn = document.getElementById('goBtn');\n" +
        "                if (goBtn) {\n" +
        "                    if (goBtn.style.display !== 'none' || (typeof verified !== 'undefined' && verified)) {\n" +
        "                        notify('Clicking Continue (goBtn)...');\n" +
        "                        if (typeof proceed === 'function') { proceed(); } else { goBtn.click(); }\n" +
        "                        return;\n" +
        "                    }\n" +
        "                }\n" +
        "            }\n" +
        "\n" +
        "            // Step 2: Auto submit safelink / tp form\n" +
        "            var f = document.getElementById('tp98') || document.forms['tp1'] || document.querySelector('form[name^=\"tp\"],form[id^=\"tp\"],form[action*=\"safelink\"],form[method=\"post\"]:not(.blogsy-search-form):not(#commentform)');\n" +
        "            if (f) {\n" +
        "                if (f.querySelector('.cf-turnstile')) {\n" +
        "                    var tokenInput = f.querySelector('[name=\"cf-turnstile-response\"]');\n" +
        "                    if (!tokenInput || !tokenInput.value) {\n" +
        "                        notify('Cloudflare Turnstile: Tap Box');\n" +
        "                        return;\n" +
        "                    }\n" +
        "                }\n" +
        "                notify('Submitting Safelink Form...');\n" +
        "                f.submit();\n" +
        "                return;\n" +
        "            }\n" +
        "\n" +
        "            // Step 3: wpsafelink redirection\n" +
        "            if (typeof wpsafelink !== 'undefined' && wpsafelink) {\n" +
        "                notify('Redirecting to Safelink...');\n" +
        "                location.href = wpsafelink;\n" +
        "                return;\n" +
        "            }\n" +
        "\n" +
        "            // Step 4: Continue buttons / safelink links\n" +
        "            var b = document.querySelector('#tp-continue,a.tp-btn,button.tp-btn:not(#tp-verify):not(#btnx),[onclick*=\"wpsafelink\"],[onclick*=\"submit\"],[href*=\"?safelink\"]');\n" +
        "            if (b && b.href && !b.href.endsWith('#')) {\n" +
        "                notify('Navigating Safelink...');\n" +
        "                location.href = b.href;\n" +
        "                return;\n" +
        "            }\n" +
        "            if (b) {\n" +
        "                b.disabled = false;\n" +
        "                b.style.display = 'inline-block';\n" +
        "                notify('Clicking Continue (tp-btn)...');\n" +
        "                b.click();\n" +
        "                return;\n" +
        "            }\n" +
        "\n" +
        "            // Step 5: Verify button\n" +
        "            var v = document.getElementById('tp-verify');\n" +
        "            if (v) {\n" +
        "                notify('Clicking tp-verify...');\n" +
        "                v.click();\n" +
        "            }\n" +
        "\n" +
        "            // Step 6: Final btnx button\n" +
        "            var x = document.getElementById('btnx');\n" +
        "            if (x) {\n" +
        "                x.disabled = false;\n" +
        "                x.style.display = 'inline-block';\n" +
        "                notify('Clicking Final btnx...');\n" +
        "                if (window.AndroidBridge && typeof window.AndroidBridge.onFinalStepTriggered === 'function') {\n" +
        "                    window.AndroidBridge.onFinalStepTriggered();\n" +
        "                }\n" +
        "                x.click();\n" +
        "            }\n" +
        "        } catch(err) {}\n" +
        "    }\n" +
        "\n" +
        "    runLinkPaysAutomation();\n" +
        "    setInterval(runLinkPaysAutomation, 300);\n" +
        "    setInterval(checkDestination, 500);\n" +
        "\n" +
        "    try {\n" +
        "        var observer = new MutationObserver(function() {\n" +
        "            runLinkPaysAutomation();\n" +
        "            checkDestination();\n" +
        "        });\n" +
        "        if (document.body) {\n" +
        "            observer.observe(document.body, { childList: true, subtree: true, characterData: true });\n" +
        "        }\n" +
        "    } catch(e){}\n" +
        "})();";

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isRotating = false;
    private boolean isDestinationTriggering = false;
    private boolean isResetting = false;
    private boolean isHandlingError = false;
    private boolean isFinalStepTriggered = false;
    private String currentIp = "...";
    private String previousIp = "";

    // 120-Second Watchdog Timer
    private final Runnable watchdogRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRotating && !isDestinationTriggering && !isResetting) {
                mainHandler.post(() -> {
                    tvStatus.setTextColor(Color.parseColor("#FFA500"));
                    tvStatus.setText("Watchdog: timeout! Rotating IP...");
                    triggerRogueRedirectReset("timeout_120s");
                });
            }
        }
    };

    private void armWatchdog() {
        mainHandler.removeCallbacks(watchdogRunnable);
        mainHandler.postDelayed(watchdogRunnable, 120000);
    }

    private void disarmWatchdog() {
        mainHandler.removeCallbacks(watchdogRunnable);
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void onDestinationDetected(String url) {
            runOnUiThread(() -> checkAndHandleDestination(url != null ? url : "https://www.mediafire.com"));
        }

        @JavascriptInterface
        public void onFinalStepTriggered() {
            runOnUiThread(() -> {
                isFinalStepTriggered = true;
                armWatchdog();
            });
        }

        @JavascriptInterface
        public void onStatusUpdate(String status) {
            runOnUiThread(() -> {
                if (!isRotating && !isDestinationTriggering && !isResetting && status != null) {
                    armWatchdog();
                    tvStatus.setTextColor(Color.parseColor("#00E676"));
                    tvStatus.setText(status);
                }
            });
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        btnAirplane = findViewById(R.id.btn_airplane);
        btnReload = findViewById(R.id.btn_reload);
        tvStatus = findViewById(R.id.tv_status);
        tvIp = findViewById(R.id.tv_ip);
        progressBar = findViewById(R.id.progress_bar);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "LinkPays::WakeLock");
            wakeLock.acquire(12 * 60 * 60 * 1000L);
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        if (WebViewFeature.isFeatureSupported(WebViewFeature.REQUESTED_WITH_HEADER_ALLOW_LIST)) {
            WebSettingsCompat.setRequestedWithHeaderOriginAllowList(settings, Collections.emptySet());
        }

        // Random flagship mobile UA from 8 devices pool
        try {
            String initialUa = MOBILE_USER_AGENTS[RANDOM.nextInt(MOBILE_USER_AGENTS.length)];
            settings.setUserAgentString(initialUa);
        } catch (Exception ignored) {
            String baseUa = WebSettings.getDefaultUserAgent(this);
            String cleanUa = baseUa.replace("; wv", "").replaceAll("Version/\\d+\\.\\d+\\s*", "");
            settings.setUserAgentString(cleanUa);
        }

        webView.addJavascriptInterface(new WebAppInterface(), "AndroidBridge");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
                String curUrl = view.getUrl();
                if (newProgress >= 15 && curUrl != null && !curUrl.equals("about:blank")) {
                    if (!curUrl.contains("/cdn-cgi/") && !curUrl.contains("challenges.cloudflare.com")) {
                        view.evaluateJavascript(SCRIPT_EARLY_SHIELD, null);
                        view.evaluateJavascript(SCRIPT_LINKPAYS, null);
                    }
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                result.confirm(defaultValue);
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleUrl(view, request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleUrl(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url != null && !url.equals("about:blank")) {
                    if (url.contains("/cdn-cgi/") || url.contains("challenges.cloudflare.com")) {
                        tvStatus.setTextColor(Color.parseColor("#FFA500"));
                        tvStatus.setText("Cloudflare Challenge: Tap Box");
                        armWatchdog();
                        return;
                    }
                    if (checkAndHandleDestination(url)) return;
                    if (isKnownRogueAdUrl(url)) {
                        return;
                    }
                    view.evaluateJavascript(SCRIPT_EARLY_SHIELD, null);
                    view.evaluateJavascript(SCRIPT_LINKPAYS, null);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (url != null && !url.equals("about:blank")) {
                    checkAndHandleDestination(url);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                if (url == null || url.equals("about:blank")) return;

                if (url.contains("/cdn-cgi/") || url.contains("challenges.cloudflare.com")) {
                    tvStatus.setTextColor(Color.parseColor("#FFA500"));
                    tvStatus.setText("Cloudflare Challenge: Tap Box");
                    return;
                }

                if (checkAndHandleDestination(url)) return;

                view.evaluateJavascript(SCRIPT_EARLY_SHIELD, null);
                view.evaluateJavascript(SCRIPT_LINKPAYS, null);
                armWatchdog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (failingUrl != null && (failingUrl.equals(TARGET_URL) || failingUrl.contains("linkpays.in") || failingUrl.contains("roadtaxcalculatorr.my"))) {
                    handleConnectionError();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    String failingUrl = request.getUrl().toString();
                    if (failingUrl.equals(TARGET_URL) || failingUrl.contains("linkpays.in") || failingUrl.contains("roadtaxcalculatorr.my")) {
                        handleConnectionError();
                    }
                }
            }
        });

        btnAirplane.setOnClickListener(v -> startAirplaneReset());

        btnReload.setOnClickListener(v -> {
            disarmWatchdog();
            refreshIpAndLoad(true);
        });

        checkRootAccess();
        refreshIpAndLoad(true);
    }

    private void handleConnectionError() {
        if (isRotating || isDestinationTriggering || isResetting || isHandlingError) return;
        isHandlingError = true;
        disarmWatchdog();

        mainHandler.post(() -> {
            tvStatus.setTextColor(Color.parseColor("#FFA500"));
            tvStatus.setText("Connection Lost: Waiting 4G...");
        });

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                try { Thread.sleep(700); } catch (InterruptedException ignored) {}
                if (isInternetAvailable()) break;
            }
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            mainHandler.post(() -> {
                isHandlingError = false;
                if (!isRotating && webView != null) {
                    tvStatus.setTextColor(Color.parseColor("#00E676"));
                    tvStatus.setText("Reloading...");
                    webView.loadUrl(TARGET_URL);
                }
            });
        }).start();
    }

    private boolean isKnownRogueAdUrl(String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();
        if (lower.contains("linkpays.in") || lower.contains("roadtaxcalculatorr.my") ||
            lower.contains("redbhoot505.workers.dev") ||
            lower.contains("cloudflare.com") || lower.contains("/cdn-cgi/") ||
            lower.contains("mediafire.com") || lower.contains("google.")) {
            return false;
        }
        return lower.contains("omg10.com") ||
               lower.contains("popads") ||
               lower.contains("adsterra") ||
               lower.contains("bet365") ||
               lower.contains("1xbet") ||
               lower.contains("trafficjunky") ||
               lower.contains("exoclick") ||
               lower.contains("hilltopads") ||
               lower.contains("propellerads") ||
               lower.contains("clickadu") ||
               lower.contains("monetag") ||
               lower.contains("coinhive") ||
               lower.contains("doubleclick") ||
               lower.contains("adtrue") ||
               lower.contains("syndication");
    }

    private boolean handleUrl(WebView view, String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();

        if (checkAndHandleDestination(url)) {
            return true;
        }

        if (isKnownRogueAdUrl(url)) {
            mainHandler.post(() -> {
                tvStatus.setText("Blocked ad: " + (url.length() > 25 ? url.substring(0, 25) + "..." : url));
            });
            return true;
        }

        // Handle Android Chrome Intent schemes
        if (url.startsWith("intent://")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                if (fallbackUrl != null && !fallbackUrl.isEmpty()) {
                    view.loadUrl(fallbackUrl);
                    return true;
                }
                String raw = url.substring("intent://".length());
                int hashIdx = raw.indexOf("#Intent;");
                if (hashIdx != -1) raw = raw.substring(0, hashIdx);
                String scheme = url.contains("scheme=http;") ? "http" : "https";
                view.loadUrl(scheme + "://" + raw);
                return true;
            } catch (Exception ignored) {}
        }

        // If final step was clicked, external navigation is destination!
        if (isFinalStepTriggered && !lower.contains("linkpays.in") && !lower.contains("roadtaxcalculatorr.my")) {
            checkAndHandleDestination(url);
            return false;
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return false;
        }
        return true;
    }

    private synchronized void triggerRogueRedirectReset(String rogueUrl) {
        if (isRotating || isDestinationTriggering || isResetting) return;
        isResetting = true;
        disarmWatchdog();

        mainHandler.post(() -> {
            try {
                if (webView != null) {
                    webView.stopLoading();
                    webView.loadUrl("about:blank");
                }
            } catch (Exception ignored) {}

            tvStatus.setTextColor(Color.parseColor("#FF5252"));
            tvStatus.setText("Resetting...");
            Toast.makeText(MainActivity.this, "Resetting shortener...", Toast.LENGTH_SHORT).show();

            mainHandler.postDelayed(() -> {
                isResetting = false;
                startAirplaneReset();
            }, 1500);
        });
    }

    private synchronized boolean checkAndHandleDestination(String url) {
        if (url == null) return false;
        String lower = url.toLowerCase();
        boolean isDest = lower.contains("mediafire.com") || lower.contains("mega.nz") || lower.contains("drive.google.com");
        if (!isDest && isFinalStepTriggered && !lower.contains("linkpays.in") && !lower.contains("roadtaxcalculatorr.my") && !lower.contains("cloudflare.com")) {
            isDest = true;
        }
        if (!isDest) return false;

        if (isRotating || isDestinationTriggering || isResetting) {
            return true;
        }
        isDestinationTriggering = true;
        disarmWatchdog();

        mainHandler.post(() -> {
            tvStatus.setTextColor(Color.parseColor("#00E676"));
            tvStatus.setText("✅ Destination! Dwell 5s...");
            Toast.makeText(MainActivity.this, "✅ Destination Reached! Dwell 5s...", Toast.LENGTH_SHORT).show();

            mainHandler.postDelayed(() -> {
                isDestinationTriggering = false;
                isFinalStepTriggered = false;
                wipeSession();
                startAirplaneReset();
            }, 5000);
        });
        return true;
    }

    private void checkRootAccess() {
        new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "echo root_ok"});
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = r.readLine();
                p.waitFor();
                if ("root_ok".equals(line)) {
                    runRootCommand("dumpsys deviceidle whitelist +" + getPackageName());
                    mainHandler.post(() -> {
                        tvStatus.setTextColor(Color.parseColor("#00E676"));
                        tvStatus.setText("Root Ready");
                    });
                }
            } catch (Exception ignored) {}
        }).start();
    }

    private boolean runRootCommand(String command) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            int exit = p.waitFor();
            return (exit == 0);
        } catch (Exception ignored) {
            return false;
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        btnAirplane.setEnabled(enabled);
        btnReload.setEnabled(enabled);
    }

    private void startAirplaneReset() {
        if (isRotating) return;
        isRotating = true;
        isDestinationTriggering = false;
        isResetting = false;
        isHandlingError = false;
        isFinalStepTriggered = false;
        disarmWatchdog();

        mainHandler.post(() -> {
            setButtonsEnabled(false);
            tvStatus.setTextColor(Color.parseColor("#FFA500"));
            tvStatus.setText("Airplane ON...");
        });

        new Thread(() -> {
            runRootCommand("cmd connectivity airplane-mode enable || settings put global airplane_mode_on 1");

            // Randomize Android ID via Root
            try {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 16; i++) {
                    sb.append(Integer.toHexString(RANDOM.nextInt(16)));
                }
                runRootCommand("settings put secure android_id " + sb.toString());
            } catch (Exception ignored) {}

            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

            mainHandler.post(() -> tvStatus.setText("Airplane OFF..."));
            runRootCommand("cmd connectivity airplane-mode disable || settings put global airplane_mode_on 0");

            mainHandler.post(() -> tvStatus.setText("Connecting 4G..."));

            boolean connected = false;
            for (int i = 0; i < 45; i++) {
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                if (isInternetAvailable()) {
                    connected = true;
                    break;
                }
            }

            if (!connected) {
                mainHandler.post(() -> tvStatus.setText("Awaiting 4G..."));
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
            } else {
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }

            String newIp = fetchPublicIp();

            // Duplicate IP Check: Auto re-toggle if same carrier IP received
            if (!previousIp.isEmpty() && !previousIp.equals("...") && !previousIp.equals("Connected") && newIp.equals(previousIp)) {
                mainHandler.post(() -> tvStatus.setText("Same IP! Re-toggling Airplane..."));
                runRootCommand("cmd connectivity airplane-mode enable || settings put global airplane_mode_on 1");
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
                runRootCommand("cmd connectivity airplane-mode disable || settings put global airplane_mode_on 0");
                for (int i = 0; i < 35; i++) {
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                    if (isInternetAvailable()) break;
                }
                newIp = fetchPublicIp();
            }
            previousIp = newIp;

            final String finalIp = newIp;
            mainHandler.post(() -> {
                currentIp = finalIp;
                tvIp.setText("IP: " + finalIp);
                tvStatus.setText("Ready");
                setButtonsEnabled(true);
                isRotating = false;

                refreshIpAndLoad(true);
            });
        }).start();
    }

    private boolean isInternetAvailable() {
        String[] testUrls = new String[]{
            "http://connectivitycheck.gstatic.com/generate_204",
            "http://clients3.google.com/generate_204",
            "https://api.ipify.org"
        };
        for (String testUrl : testUrls) {
            try {
                HttpURLConnection conn = (HttpURLConnection) (new URL(testUrl).openConnection());
                conn.setRequestProperty("User-Agent", "Android");
                conn.setConnectTimeout(1500);
                conn.setReadTimeout(1500);
                conn.connect();
                if (conn.getResponseCode() == 204 || conn.getResponseCode() == 200) {
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    private void wipeSession() {
        try {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
            WebStorage.getInstance().deleteAllData();
            if (webView != null) {
                webView.clearHistory();
                webView.clearCache(true);
                webView.clearFormData();
                webView.clearSslPreferences();
            }
        } catch (Exception ignored) {}
    }

    private void refreshIpAndLoad(boolean reloadWeb) {
        isFinalStepTriggered = false;
        wipeSession();
        armWatchdog();

        // Rotate User-Agent from 8 Flagship Devices Pool
        try {
            String nextUa = MOBILE_USER_AGENTS[RANDOM.nextInt(MOBILE_USER_AGENTS.length)];
            if (webView != null) {
                webView.getSettings().setUserAgentString(nextUa);
            }
        } catch (Exception ignored) {
            String baseUa = WebSettings.getDefaultUserAgent(this);
            String cleanUa = baseUa.replace("; wv", "").replaceAll("Version/\\d+\\.\\d+\\s*", "");
            if (webView != null) {
                webView.getSettings().setUserAgentString(cleanUa);
            }
        }

        if (reloadWeb && webView != null) {
            progressBar.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(Color.parseColor("#29B6F6"));
            tvStatus.setText("Loading Shortener...");
            webView.loadUrl(TARGET_URL);
        }

        new Thread(() -> {
            String ip = fetchPublicIp();
            mainHandler.post(() -> {
                currentIp = ip;
                tvIp.setText("IP: " + currentIp);
            });
        }).start();
    }

    private String fetchPublicIp() {
        String[] endpoints = {
            "https://api.ipify.org",
            "https://icanhazip.com",
            "https://checkip.amazonaws.com"
        };
        for (String ep : endpoints) {
            try {
                URL url = new URL(ep);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(2500);
                conn.setReadTimeout(2500);
                if (conn.getResponseCode() == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String ip = r.readLine();
                    r.close();
                    if (ip != null && !ip.trim().isEmpty()) {
                        return ip.trim();
                    }
                }
            } catch (Exception ignored) {}
        }
        return "Connected";
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disarmWatchdog();
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        if (webView != null) {
            webView.destroy();
        }
    }
}
