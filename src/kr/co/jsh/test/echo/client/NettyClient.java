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
		//EventLoop���� ����� group ����
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			//���Ͽ����� �� BootStrap
			//bootstrap�� �޼ҵ� ü�̴� ����
			Bootstrap b = new Bootstrap();
			b.group(group); //Ŭ���̾�Ʈ �̺�Ʈ�� ó���� EventLoopGroup�� ����
			b.channel(NioSocketChannel.class); //ä�� ���� ���� : NIO�� ����
			b.remoteAddress(new InetSocketAddress(ip,port)); //������ InetSocketAddress�� �̿��� ������ ������ IP�� ���´�. (echo�� ��쿡�� ��Ʈ�� ������ �ȴ�.)
			b.handler(new ChannelInitializer<SocketChannel>() { //Bootstrap�� �����ϴ� handler�� ChannelInitializer�� ���-> �� ����� �ǹ̸� �ؼ��� �ʿ䰡 �ִ�.
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//Handler ��� �����ϰ� �ϱ� ���� ���������� ���� ����
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new NettyClientHandler());
				}
			});
			
			try {
				
				URI uri = new URI("http://192.168.1.200:5517");
				FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri.getRawPath());
				
				//��� ����
				request.headers().set("Content-Type", "application/json");
				request.headers().set("Accept"		, "*/*"				);
				
				Channel ch = b.connect().sync().channel();
				ch.writeAndFlush(request);
				ch.closeFuture().sync();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//			ChannelFuture f = b.connect().sync(); //���� �Ǿ�� �����ϰ� ������ �Ϸ�Ǳ⸦ ��ٸ�
//			f.channel().closeFuture().sync(); //ä���� ���� ������ ���ŷ��
			
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
