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
			b.channel(NioServerSocketChannel.class); //NIO ����ä���� �̿��ϵ��� ����
			b.localAddress(new InetSocketAddress(7878)); //������ ��Ʈ�� ���� �ּ� ����
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(serverHandler); //
				}
			});
			
			//������ �񵿱� ó���� �ޱ� ���� Future ���
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
			
		} finally {
			group.shutdownGracefully().sync();
		}
		
	}

}
