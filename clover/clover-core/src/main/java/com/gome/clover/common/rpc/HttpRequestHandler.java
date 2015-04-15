package com.gome.clover.common.rpc;

import com.gome.clover.common.tools.StringUtil;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:Http Request Handler
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler
{
    private static final String RESPONSE_TEMPLATE = "{success:%d, m:'%s'}";
    private static final String PATTERN = "([^?|!]*)(.*)";

    private String decodeUri(String uri) {
        try {
            return URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                return URLDecoder.decode(uri, "ISO-8859-1"); } catch (UnsupportedEncodingException e1) {
            }
        }
        return uri;
    }

    private static String getParameterFromUri(String startPrefix, String uri)
    {
        Pattern pattern = Pattern.compile(startPrefix + "([^?|!]*)(.*)",
                2);
        Matcher matcher = pattern.matcher(uri);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception
    {
        HttpRequest request = (HttpRequest)e.getMessage();
        String uri = decodeUri(request.getUri());
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        Map params = queryStringDecoder.getParameters();
        String msg;
        if (uri.startsWith("/push/")) {
            msg = getParameterValue("msg", params);
            if(StringUtil.isEmpty(msg)) return;
            System.err.println("msg:"+msg);
            writeResponse(e, HttpResponseStatus.OK, String.format("{success:%d, msg:'%s'}",
                    new Object[] { Integer.valueOf(1), Long.valueOf(1)}),request);
        }  else {

        }

    }

    private String getParameterValue(String parameterName, Map<String, List<String>> params)
    {
        List values = (List)params.get(parameterName);
        String parameterValue = null;
        if ((values != null) && (!values.isEmpty())) {
            parameterValue = (String)values.get(0);
        }
        return parameterValue;
    }

    private void writeResponse(MessageEvent e, HttpResponseStatus httpResponseStatus, String bufString, HttpRequest request)
    {
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
                httpResponseStatus);
        response.setContent(ChannelBuffers.copiedBuffer(bufString,
                CharsetUtil.UTF_8));
        response.setHeader("Content-Type", "application/json; charset=UTF-8");
        ChannelFuture future = e.getChannel().write(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception
    {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}