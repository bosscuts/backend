package com.bosscut.config;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.auth.AuthType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class ProxyConfiguration {
    @Bean(name = "browserMobProxy")
    public BrowserMobProxy browserMobProxy() {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.setChainedProxy(new InetSocketAddress("103.176.112.191", 22222));
        proxy.chainedProxyAuthorization("ProxyVN271310", "mvpZX3Jz", AuthType.BASIC);
        proxy.setTrustAllServers(true);
        proxy.start(0);
        return proxy;
    }

}
