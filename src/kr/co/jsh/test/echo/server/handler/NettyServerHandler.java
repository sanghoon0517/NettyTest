package kr.co.jsh.test.echo.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter{

	String className = this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);

	@Override //메시지가 들어올 때마다 호출된다.
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf)msg;
		System.out.println("["+this.className+"] Server received : "+in.toString(CharsetUtil.UTF_8));
		ctx.write(in); //클라이언트로부터 받은 메시지를 다시 Echo시킨다.
		//아웃바운드 메시지를 플러시하지 않은 채로 받은 메시지를 발신자로 출력한다.
	}
	
	//Channel에서 읽기 작업이 완료시 호출
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("읽기 작업이 완료됨 : ["+this.className+"]");
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //대기중인 메시지를 플러시하고 채널을 닫음
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override //읽기 작업 중 예외가 발생하면 호출된다.
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close(); //예외가 발생했을 시, 예외를 잡고 채널 context를 닫아버림
	}
	
	//서버와 연결 성공시 호출되는 메서드
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.write(Unpooled.copiedBuffer("Netty Connect() : [서버와 연결 성공]", CharsetUtil.UTF_8)); //채널 활성화 시 메시지 전송
	}
	
	//서버와 연결 실패시 호출되는 메서드
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Fail to Connect() : [서버와 연결 실패]", CharsetUtil.UTF_8)); //채널 활성화 시 메시지 전송
	}
	
	//ChannelHandler가 ChannelPipeline에 추가될 때 호출되는 메서드
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to pipeline : ["+this.className+"클래스를 ChannelPipeline에 추가]", CharsetUtil.UTF_8)); //채널 활성화 시 메시지 전송
	}
	
	//Channel이 EventLoop에 등록되고 입출력을 처리할 수 있으면 호출되는 메서드
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"클래스를 EventLoop에 추가] 입출력 처리 가능", CharsetUtil.UTF_8));
	}
	
	//Channel이 EventLoop에 해제되고 입출력을 처리할 수 없으면 호출되는 메서드
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"클래스를 EventLoop에 해제] 입출력 처리 불가능", CharsetUtil.UTF_8));
	}
	
}
