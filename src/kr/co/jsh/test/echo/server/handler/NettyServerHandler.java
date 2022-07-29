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

	@Override //�޽����� ���� ������ ȣ��ȴ�.
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf)msg;
		System.out.println("["+this.className+"] Server received : "+in.toString(CharsetUtil.UTF_8));
		ctx.write(in); //Ŭ���̾�Ʈ�κ��� ���� �޽����� �ٽ� Echo��Ų��.
		//�ƿ��ٿ�� �޽����� �÷������� ���� ä�� ���� �޽����� �߽��ڷ� ����Ѵ�.
	}
	
	//Channel���� �б� �۾��� �Ϸ�� ȣ��
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("�б� �۾��� �Ϸ�� : ["+this.className+"]");
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //������� �޽����� �÷����ϰ� ä���� ����
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override //�б� �۾� �� ���ܰ� �߻��ϸ� ȣ��ȴ�.
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close(); //���ܰ� �߻����� ��, ���ܸ� ��� ä�� context�� �ݾƹ���
	}
	
	//������ ���� ������ ȣ��Ǵ� �޼���
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.write(Unpooled.copiedBuffer("Netty Connect() : [������ ���� ����]", CharsetUtil.UTF_8)); //ä�� Ȱ��ȭ �� �޽��� ����
	}
	
	//������ ���� ���н� ȣ��Ǵ� �޼���
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Fail to Connect() : [������ ���� ����]", CharsetUtil.UTF_8)); //ä�� Ȱ��ȭ �� �޽��� ����
	}
	
	//ChannelHandler�� ChannelPipeline�� �߰��� �� ȣ��Ǵ� �޼���
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to pipeline : ["+this.className+"Ŭ������ ChannelPipeline�� �߰�]", CharsetUtil.UTF_8)); //ä�� Ȱ��ȭ �� �޽��� ����
	}
	
	//Channel�� EventLoop�� ��ϵǰ� ������� ó���� �� ������ ȣ��Ǵ� �޼���
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"Ŭ������ EventLoop�� �߰�] ����� ó�� ����", CharsetUtil.UTF_8));
	}
	
	//Channel�� EventLoop�� �����ǰ� ������� ó���� �� ������ ȣ��Ǵ� �޼���
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty added to EventLoop : ["+this.className+"Ŭ������ EventLoop�� ����] ����� ó�� �Ұ���", CharsetUtil.UTF_8));
	}
	
}
