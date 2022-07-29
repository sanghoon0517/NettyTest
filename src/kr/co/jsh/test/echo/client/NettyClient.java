package kr.co.jsh.test.echo.client;

import java.net.InetSocketAddress;
import java.net.URI;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import kr.co.jsh.test.echo.client.handler.NettyClientHandler;

public class NettyClient {
	
	final private String ip = "192.168.1.200";
	final private int port = 5517;
	
	public static void main(String[] args) throws Exception{
		new NettyClient().start();
	}
	
	public void start() throws Exception{
		//EventLoop들을 등록할 group 생성
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			//소켓역할을 할 BootStrap
			//bootstrap은 메소드 체이닝 가능
			Bootstrap b = new Bootstrap();
			b.group(group); //클라이언트 이벤트를 처리할 EventLoopGroup을 지정
			b.channel(NioSocketChannel.class); //채널 유형 지정 : NIO로 지정
			b.remoteAddress(new InetSocketAddress(ip,port)); //서버의 InetSocketAddress를 이용해 접속할 서버와 IP를 적는다. (echo인 경우에는 포트만 적으면 된다.)
			b.handler(new ChannelInitializer<SocketChannel>() { //Bootstrap에 존재하는 handler에 ChannelInitializer를 등록-> 이 경우의 의미를 해석할 필요가 있다.
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//Handler 등록 간편하게 하기 위해 파이프라인 따로 받음
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new NettyClientHandler());
				}
			});
			
			try {
				
				URI uri = new URI("http://192.168.1.200:5517");
				FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.getRawPath());
				
				//헤더 셋팅
				request.headers().set("Content-Type", "application/json");
				request.headers().set("Accept"		, "*/*"				);
				
				Channel ch = b.connect().sync().channel();
				ch.writeAndFlush(request);
				ch.closeFuture().sync();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			ChannelFuture f = b.connect().sync(); //원격 피어로 연결하고 연결이 완료되기를 기다림
//			f.channel().closeFuture().sync(); //채널이 닫힐 때까지 블로킹함
			
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
