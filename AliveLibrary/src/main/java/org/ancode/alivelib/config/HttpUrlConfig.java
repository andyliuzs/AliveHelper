package org.ancode.alivelib.config;

/**
 * Created by andyliu on 16-10-11.
 */
public class HttpUrlConfig {
    public static final String ALIVE_STATS_POST_HOST = "xz.mixun.org";
    public static final String GET_WARNING_HTML_URL = "http://" + ALIVE_STATS_POST_HOST + "/app/alivehelper/help/query";
    public static final String GET_WARNING_HTML_ANET_URL = "http://" + ALIVE_STATS_POST_HOST + "/app/alivehelper/help/query";
    public static final String POST_ALIVE_STATS_URL = "http://" + ALIVE_STATS_POST_HOST + "/app/alivehelper/stats/upload";
    public static final String QUERY_ALIVE_STATS_URL = "http://" + ALIVE_STATS_POST_HOST + "/stats/query";
    public static final String DEFAULT_WARNING_URL = "http://xz.mixun.org/app/alivehelper/static/default/default/index.html";
}
