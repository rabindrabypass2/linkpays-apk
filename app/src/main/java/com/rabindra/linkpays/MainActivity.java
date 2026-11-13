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
        "            if (s.indexOf('linkpays.in') !== -1 || s.indexOf('rank1st.in') !== -1 || s.indexOf('roadtaxcalculatorr.my') !== -1 || s.indexOf('cloudflare') !== -1 || s.indexOf('mediafire.com') !== -1) {\n" +
        "                _origReplace(u);\n" +
        "            }\n" +
        "        };\n" +
        "        window.blurred = false;\n" +
        "        window.onblur = null;\n" +
        "    } catch(e){}\n" +
        "})();";

    // dj's Full Automated Chain-Skip Script
    private static final String SCRIPT_CHAIN_SKIP =
        "(function(){\n" +
        "    if (window.__chainskip_running) return;\n" +
        "    window.__chainskip_running = true;\n" +
        "\n" +
        "    // 1. Anti-Fingerprint\n" +
        "    try {\n" +
        "        Object.defineProperty(navigator, 'hardwareConcurrency', { get: function() { return 8; } });\n" +
        "        Object.defineProperty(navigator, 'deviceMemory', { get: function() { return 8; } });\n" +
        "        Object.defineProperty(navigator, 'maxTouchPoints', { get: function() { return 5; } });\n" +
        "    } catch(e){}\n" +
        "\n" +
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
        "    // 2. Chain-Skip Engine\n" +
        "    var D = document, L = location;\n" +
        "    var MAX_HOPS = 12;\n" +
        "    var FETCH_MS = 15000;\n" +
        "    var FALLBACK_TOKEN = 'hMB0hZ';\n" +
        "    var FILE_HOST = /(?:mediafire\\.com|mega\\.nz|mega\\.co\\.nz|drive\\.google\\.com|dropbox\\.com|apkpure\\.com|uptodown\\.com|apkmirror\\.com|pixeldrain\\.com|gofile\\.io|1drv\\.ms|terabox\\.com|zippyshare\\.com)/i;\n" +
        "    var GATEWAY = /secure\\s*gateway/i;\n" +
        "    var badge = null;\n" +
        "\n" +
        "    function say(msg) {\n" +
        "        try {\n" +
        "            if (window.AndroidBridge && typeof window.AndroidBridge.onStatusUpdate === 'function') {\n" +
        "                window.AndroidBridge.onStatusUpdate(msg);\n" +
        "            }\n" +
        "            if (!badge || !badge.isConnected) {\n" +
        "                badge = D.createElement('div');\n" +
        "                badge.setAttribute('style', 'position:fixed;z-index:2147483647;left:14px;bottom:14px;max-width:min(520px,calc(100vw - 28px));padding:13px 16px;border-radius:10px;background:#0b1220;color:#7dd3fc;font:700 13px/1.45 system-ui,Segoe UI,sans-serif;box-shadow:0 12px 34px rgba(0,0,0,.55)');\n" +
        "                (D.body || D.documentElement).appendChild(badge);\n" +
        "            }\n" +
        "            badge.textContent = '';\n" +
        "            var line = D.createElement('div');\n" +
        "            line.textContent = 'chain-skip - ' + msg;\n" +
        "            badge.appendChild(line);\n" +
        "        } catch (e) {}\n" +
        "    }\n" +
        "\n" +
        "    function panel(msg, dest) {\n" +
        "        say(msg);\n" +
        "        if (dest && window.AndroidBridge && typeof window.AndroidBridge.onDestinationDetected === 'function') {\n" +
        "            window.AndroidBridge.onDestinationDetected(dest);\n" +
        "        }\n" +
        "        if (dest) {\n" +
        "            say('Auto-Opening: ' + dest);\n" +
        "            setTimeout(function() { L.href = dest; }, 600);\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    function abs(u, base) {\n" +
        "        try { return new URL(String(u).replace(/\\\\\\//g, '/').replace(/&amp;/g, '&').trim(), base || L.href).href; } catch (e) { return ''; }\n" +
        "    }\n" +
        "    function originOf(u) { try { return new URL(u).origin; } catch (e) { return ''; } }\n" +
        "    function hostOf(u) { try { return new URL(u).host; } catch (e) { return String(u || '').slice(0, 48); } }\n" +
        "    function flat(el) { return ((el && el.textContent) || '').replace(/\\s+/g, ' ').trim(); }\n" +
        "\n" +
        "    function token() {\n" +
        "        var m = L.search.match(/[?&]verify=([A-Za-z0-9]+)/i);\n" +
        "        if (m) return m[1];\n" +
        "        var inp = D.querySelector('input[name=\"newwpsafelink\"],input[name=\"link3\"],input[name=\"clickarlink\"]');\n" +
        "        if (inp && inp.value) return inp.value;\n" +
        "        var html = D.documentElement ? D.documentElement.innerHTML : '';\n" +
        "        m = html.match(/[?&]verify=([A-Za-z0-9]{5,16})/i);\n" +
        "        if (m) return m[1];\n" +
        "        return FALLBACK_TOKEN;\n" +
        "    }\n" +
        "\n" +
        "    function formHop(form, base) {\n" +
        "        var body = null;\n" +
        "        try { body = new FormData(form); } catch (e) { body = null; }\n" +
        "        return {\n" +
        "            url: abs(form.getAttribute('action') || base, base),\n" +
        "            method: String(form.getAttribute('method') || 'get').toUpperCase(),\n" +
        "            body: body,\n" +
        "            kind: 'form'\n" +
        "        };\n" +
        "    }\n" +
        "\n" +
        "    function hopOf(doc, base) {\n" +
        "        var i, el;\n" +
        "        var fields = doc.querySelectorAll('input[name=\"newwpsafelink\"],input[name=\"link3\"],input[name=\"clickarlink\"],input[name=\"tp98\"]');\n" +
        "        for (i = 0; i < fields.length; i++) {\n" +
        "            var form = fields[i].form || (fields[i].closest && fields[i].closest('form'));\n" +
        "            if (form) return formHop(form, base);\n" +
        "        }\n" +
        "        el = doc.getElementById('tp-snp2');\n" +
        "        if (el) {\n" +
        "            var wrap = el.closest && el.closest('a[href]');\n" +
        "            if (wrap) return { url: abs(wrap.getAttribute('href'), base), method: 'GET', body: null, kind: 'snp2-anchor' };\n" +
        "            var own = el.closest && el.closest('form');\n" +
        "            if (own) return formHop(own, base);\n" +
        "            if (el.tagName === 'A' && el.getAttribute('href')) {\n" +
        "                return { url: abs(el.getAttribute('href'), base), method: 'GET', body: null, kind: 'snp2-self' };\n" +
        "            }\n" +
        "        }\n" +
        "        var anchors = doc.querySelectorAll('a[href]');\n" +
        "        for (i = 0; i < anchors.length; i++) {\n" +
        "            if (/^continue\\s+\\d+\\s*\\/\\s*\\d+$/i.test(flat(anchors[i]))) {\n" +
        "                return { url: abs(anchors[i].getAttribute('href'), base), method: 'GET', body: null, kind: 'continue' };\n" +
        "            }\n" +
        "        }\n" +
        "        var metas = doc.querySelectorAll('meta[http-equiv]');\n" +
        "        for (i = 0; i < metas.length; i++) {\n" +
        "            if (String(metas[i].getAttribute('http-equiv')).toLowerCase() !== 'refresh') continue;\n" +
        "            var mm = String(metas[i].getAttribute('content') || '').match(/url\\s*=\\s*['\"]?([^'\";]+)/i);\n" +
        "            if (mm) return { url: abs(mm[1], base), method: 'GET', body: null, kind: 'meta' };\n" +
        "        }\n" +
        "        return null;\n" +
        "    }\n" +
        "\n" +
        "    function destination(doc) {\n" +
        "        var html = doc.documentElement ? doc.documentElement.outerHTML : '';\n" +
        "        var src = html.replace(/\\\\\\//g, '/');\n" +
        "        var m = src.match(/location\\s*\\.\\s*(?:replace|assign)\\s*\\(\\s*['\"]([^'\"]+)['\"]/i) ||\n" +
        "                src.match(/location\\s*\\.\\s*href\\s*=\\s*['\"]([^'\"]+)['\"]/i) ||\n" +
        "                src.match(/window\\s*\\.\\s*open\\s*\\(\\s*['\"]([^'\"]+)['\"]/i);\n" +
        "        if (m && /^https?:\\/\\//i.test(m[1])) return m[1];\n" +
        "        var re = /https?:\\/\\/[^\\s\"'<>\\]+/gi, g;\n" +
        "        while ((g = re.exec(src))) {\n" +
        "            if (FILE_HOST.test(g[0])) return g[0];\n" +
        "        }\n" +
        "        return '';\n" +
        "    }\n" +
        "\n" +
        "    function load(hop) {\n" +
        "        var ctl = typeof AbortController === 'function' ? new AbortController() : null;\n" +
        "        var timer = ctl ? setTimeout(function () { ctl.abort(); }, FETCH_MS) : 0;\n" +
        "        var init = { method: hop.method, credentials: 'include', redirect: 'follow', cache: 'no-store' };\n" +
        "        if (ctl) init.signal = ctl.signal;\n" +
        "        if (hop.method === 'POST' && hop.body) init.body = hop.body;\n" +
        "        return fetch(hop.url, init).then(function (r) {\n" +
        "            clearTimeout(timer);\n" +
        "            return r.text().then(function (t) {\n" +
        "                return { doc: new DOMParser().parseFromString(t, 'text/html'), url: r.url || hop.url, html: t };\n" +
        "            });\n" +
        "        }, function (e) {\n" +
        "            clearTimeout(timer);\n" +
        "            throw e;\n" +
        "        });\n" +
        "    }\n" +
        "\n" +
        "    function cross(hop) {\n" +
        "        if (hop.method === 'POST' && hop.body) {\n" +
        "            say('crossing to ' + hostOf(hop.url) + ' (form)');\n" +
        "            var f = D.createElement('form');\n" +
        "            f.method = 'POST';\n" +
        "            f.action = hop.url;\n" +
        "            f.style.display = 'none';\n" +
        "            hop.body.forEach(function (v, k) {\n" +
        "                var i = D.createElement('input');\n" +
        "                i.type = 'hidden';\n" +
        "                i.name = k;\n" +
        "                i.value = v;\n" +
        "                f.appendChild(i);\n" +
        "            });\n" +
        "            (D.body || D.documentElement).appendChild(f);\n" +
        "            f.submit();\n" +
        "            return;\n" +
        "        }\n" +
        "        say('crossing to ' + hostOf(hop.url) + '...');\n" +
        "        try { L.replace(hop.url); } catch (e) {\n" +
        "            try { L.href = hop.url; } catch (e2) {}\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    var rawSetTimeout = window.setTimeout;\n" +
        "    var rawSetInterval = window.setInterval;\n" +
        "    var rawClearTimeout = window.clearTimeout;\n" +
        "    var rawClearInterval = window.clearInterval;\n" +
        "    var keep = {};\n" +
        "\n" +
        "    function sweep(limit) {\n" +
        "        for (var i = 1; i <= limit; i++) {\n" +
        "            if (keep[i]) continue;\n" +
        "            try { rawClearTimeout(i); } catch (e) {}\n" +
        "            try { rawClearInterval(i); } catch (e) {}\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    function hold(dest) {\n" +
        "        sweep(20000);\n" +
        "        try {\n" +
        "            window.setTimeout = function () { return 0; };\n" +
        "            window.setInterval = function () { return 0; };\n" +
        "        } catch (e) {}\n" +
        "        var guard = rawSetInterval(function () { sweep(4000); }, 150);\n" +
        "        var stopper = rawSetTimeout(function () { rawClearInterval(guard); }, 8000);\n" +
        "        keep[guard] = 1;\n" +
        "        keep[stopper] = 1;\n" +
        "        try {\n" +
        "            var marks = D.querySelectorAll('b, strong, p, h2, h3, span');\n" +
        "            for (var i = 0; i < marks.length; i++) {\n" +
        "                if (marks[i].children.length) continue;\n" +
        "                if (/redirecting/i.test(marks[i].textContent || '')) {\n" +
        "                    marks[i].textContent = 'Held - Opening Destination...';\n" +
        "                }\n" +
        "            }\n" +
        "        } catch (e) {}\n" +
        "        panel('held on Get Link page - opening destination', dest);\n" +
        "    }\n" +
        "\n" +
        "    function linkpays(cur) {\n" +
        "        var dest = destination(cur.doc);\n" +
        "        if (!dest) {\n" +
        "            if (GATEWAY.test(cur.html)) {\n" +
        "                var t = token();\n" +
        "                say('Auto-Opening rank1st chain (verify=' + t + ')...');\n" +
        "                L.replace('https://rank1st.in/?verify=' + encodeURIComponent(t));\n" +
        "                return;\n" +
        "            }\n" +
        "            return say('on linkpays page, searching...');\n" +
        "        }\n" +
        "        hold(dest);\n" +
        "    }\n" +
        "\n" +
        "    async function walk() {\n" +
        "        var cur = { doc: D, url: L.href, html: D.documentElement ? D.documentElement.outerHTML : '' };\n" +
        "        var seen = {};\n" +
        "        var depth = 0;\n" +
        "        var lastPrint = '';\n" +
        "        for (;;) {\n" +
        "            if (/(^|\\.)linkpays\\.in$/i.test(hostOf(cur.url))) return linkpays(cur);\n" +
        "            if (FILE_HOST.test(hostOf(cur.url))) {\n" +
        "                if (window.AndroidBridge && typeof window.AndroidBridge.onDestinationDetected === 'function') {\n" +
        "                    window.AndroidBridge.onDestinationDetected(cur.url);\n" +
        "                }\n" +
        "                return say('Destination Reached: ' + hostOf(cur.url));\n" +
        "            }\n" +
        "            var hop = hopOf(cur.doc, cur.url);\n" +
        "            if (!hop) {\n" +
        "                var direct = destination(cur.doc);\n" +
        "                if (direct) return panel('Direct Destination', direct);\n" +
        "                return say('no gate on ' + hostOf(cur.url));\n" +
        "            }\n" +
        "            var key = hop.method + ' ' + hop.url;\n" +
        "            seen[key] = (seen[key] || 0) + 1;\n" +
        "            if (seen[key] > 3) return say('loop: ' + hostOf(hop.url) + ' repeats');\n" +
        "            depth += 1;\n" +
        "            if (depth > MAX_HOPS) return say('stopped after max hops');\n" +
        "            if (originOf(hop.url) !== originOf(L.href)) return cross(hop);\n" +
        "            say('hop ' + depth + ' - ' + hostOf(hop.url) + ' (' + hop.kind + ')');\n" +
        "            var page;\n" +
        "            try {\n" +
        "                page = await load(hop);\n" +
        "            } catch (e) {\n" +
        "                return cross(hop);\n" +
        "            }\n" +
        "            var print = page.html.length + ':' + (page.doc.title || '');\n" +
        "            if (print === lastPrint) return cross(hop);\n" +
        "            lastPrint = print;\n" +
        "            cur = page;\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    try {\n" +
        "        walk();\n" +
        "    } catch (e) {\n" +
        "        say('stopped: ' + (e && e.message ? e.message : 'error'));\n" +
        "    }\n" +
        "})();";

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isRotating = false;
    private boolean isDestinationTriggering = false;
    private boolean isResetting = false;
    private boolean isHandlingError = false;
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
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

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
                        view.evaluateJavascript(SCRIPT_CHAIN_SKIP, null);
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
                        tvStatus.setText("Cloudflare Challenge");
                        armWatchdog();
                        return;
                    }
                    if (checkAndHandleDestination(url)) return;
                    if (isKnownRogueAdUrl(url)) {
                        return;
                    }
                    view.evaluateJavascript(SCRIPT_EARLY_SHIELD, null);
                    view.evaluateJavascript(SCRIPT_CHAIN_SKIP, null);
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
                    tvStatus.setText("Cloudflare Challenge");
                    return;
                }

                if (checkAndHandleDestination(url)) return;

                view.evaluateJavascript(SCRIPT_EARLY_SHIELD, null);
                view.evaluateJavascript(SCRIPT_CHAIN_SKIP, null);
                armWatchdog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (failingUrl != null && (failingUrl.equals(TARGET_URL) || failingUrl.contains("linkpays.in") || failingUrl.contains("rank1st.in"))) {
                    handleConnectionError();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) {
                    String failingUrl = request.getUrl().toString();
                    if (failingUrl.equals(TARGET_URL) || failingUrl.contains("linkpays.in") || failingUrl.contains("rank1st.in")) {
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
        if (lower.contains("linkpays.in") || lower.contains("rank1st.in") ||
            lower.contains("roadtaxcalculatorr.my") ||
            lower.contains("cloudflare.com") || lower.contains("/cdn-cgi/") ||
            lower.contains("mediafire.com") || lower.contains("mega.nz") ||
            lower.contains("drive.google.com") || lower.contains("google.")) {
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
        boolean isDest = lower.contains("mediafire.com") || lower.contains("mega.nz") ||
                         lower.contains("drive.google.com") || lower.contains("dropbox.com") ||
                         lower.contains("apkpure.com") || lower.contains("pixeldrain.com") ||
                         lower.contains("gofile.io") || lower.contains("terabox.com");
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
