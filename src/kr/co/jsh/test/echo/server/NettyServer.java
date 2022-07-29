package kr.co.jsh.test.echo.server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import kr.co.jsh.test.echo.server.handler.NettyServerHandler;

public class NettyServer {
	
	private final int port;
	public NettyServer() {
		this.port = 7878;
	}
	
	public static void main(String[] args) throws Exception{
		new NettyServer().start();
	}
	
	private void start() throws Exception {
		final NettyServerHandler serverHandler = new NettyServerHandler();
		
		EventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		
		
		try {
			b.group(group);
			b.channel(NioServerSocketChannel.class); //NIO 전송채널을 이용하도록 지정
			b.localAddress(new InetSocketAddress(7878)); //지정된 포트로 소켓 주소 설정
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(serverHandler); //
				}
			});
			
			//서버를 비동기 처리로 받기 위해 Future 사용
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
			
		} finally {
			group.shutdownGracefully().sync();
		}
		
	}

}
