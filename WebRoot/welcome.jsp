<%@page import="com.sttri.util.Constant"%>
<%@page import="com.sttri.pojo.TblControl"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
List<TblControl> list = (ArrayList<TblControl>)request.getAttribute("list");
if(list == null || list.size() ==0){
	list = new ArrayList<TblControl>();
}
String androidUrl = Constant.readKey("androidUrl");
String pcUrl = Constant.readKey("pcUrl");
for(int i=0;i<list.size();i++){
	if(list.get(i).getConType()==6){
		androidUrl += list.get(i).getConPath();
	}
	if(list.get(i).getConType()==3){
		pcUrl += list.get(i).getConPath();
	}
}
%>

<!DOCTYPE html>
<html>
	<head>
		<title>视频直播</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- ie使用最高版本,启用GCF -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >
		<!-- 360启用急速模式 -->
		<meta name="renderer" content="webkit">
		<!-- 不缓存  -->
		<meta http-equiv="pragma" content="no-cache"> 
		<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
		<meta http-equiv="expires" content="0">
		
		<link href="welcom/css/bootstrap.css" rel='stylesheet' type='text/css' />
		<link href="welcom/css/style.css" rel="stylesheet" type="text/css" media="all" />
		
		<!--[if lt IE 9]>
		  <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
		  <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->
		
		<script type="text/javascript" src="welcom/js/jquery.min.js"></script>
		<script type="text/javascript" src="welcom/js/jquery.easing.min.js"></script>
		<script type="text/javascript" src="welcom/js/go-top.js"></script>
		<script type="text/javascript" src="welcom/js/jquery.sticky.js"></script>
		<script type="text/javascript" src="welcom/js/custom.js"></script>
		<script type="text/javascript" src="welcom/js/qrcode.js"></script>
		
		<style type="text/css">
			a#go-top{background:#E6E6E6;width:50px;height:25px;text-align:center;text-decoration:none;line-height:25px;color:#999;}
			a#go-top:hover{background:#CCC;color:#333;}
			
			#navigation{
				background: #fff none repeat scroll 0 0;
				border-bottom: 1px solid #eee;
			    box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
			    box-sizing: border-box;
			    width: 100%;
			    z-index: 9999;
			}
		</style>
		<script type="text/javascript">
			function makeQRCode() {
				var url = "<%=androidUrl %>";
	            var qrcode = new QRCode("preview",url,"150","150");
	            qrcode.makeCode(url);
	        }
	        
		</script>
	</head>

	<body id="index" onload="javascript:makeQRCode();">
		<!-- Navigation -->
		<div id="navigation">
			<nav class="navbar navbar-custom" role="navigation">
				<div class="container">
					<div class="row">
						<div class="col-md-2">
							<div class="site-logo">
								<img alt="" src="welcom/images/logo.png">
								<img alt="" src="welcom/images/logo_sec.png">
							</div>
						</div>
						<div class="col-md-10">
							<!-- Brand and toggle get grouped for better mobile display -->
							<div class="navbar-header">
								<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#menu">
									<i class="fa fa-bars"></i>
								</button>
							</div>
							<!-- Collect the nav links, forms, and other content for toggling -->
							<!--<div class="collapse navbar-collapse" id="menu">
								<ul class="nav navbar-nav navbar-right">
									<li class="active"><a href="index1.html">首页</a></li>
									<li><a href="#about">业务介绍</a></li>
									<li><a href="#features">功能介绍</a></li>
									<li><a href="#user">成功案例</a></li>
									<li><a href="#footer">联系我们</a></li>
								</ul>
							</div>-->
							<div class="top-menu">
								<span class="menu"></span>
								<ul>
									<li><a class="active hvr-shutter-out-horizontal" href="#index"><span style="color: #000000;">首页</span></a></li>
									<li><a class="hvr-shutter-out-horizontal" href="#about"><span style="color: #000000;">业务介绍</span></a></li>
									<li><a class="hvr-shutter-out-horizontal" href="#features"><span style="color: #000000;">功能介绍</span></a></li>
									<li><a class="hvr-shutter-out-horizontal" href="#user"><span style="color: #000000;">应用场景</span></a></li>
									<!-- <li><a class="hvr-shutter-out-horizontal" href="#problem"><span style="color: #000000;">常见问题</span></a></li> -->
									<li><a class="hvr-shutter-out-horizontal" href="#footer"><span style="color: #000000;">联系我们</span></a></li>
									<li><a class="hvr-shutter-out-horizontal" href="login.jsp"><span style="font-weight:bold;color: #000000;">企业登录</span></a></li>
								</ul>
							</div>
							<!-- /.Navbar-collapse -->
						</div>
					</div>
				</div>
				<!-- /.container -->
			</nav>
		</div>
		<!-- /Navigation -->
		<!--header-->
		<div class="banner">
			<div class="container">
				<div class="banner-top">
				</div>
				<div class="banner-bottom">
					<div class="col-md-6 banner-left">
						<h1>即拍即传</h1>
						<br />
						<h3>拍+传=<span>你的需要</span></h3>
						<p>即拍即传是一类现场直播服务产品，满足客户对视频内容录制并实时传播等需求，通过终端拍摄视频并接入网络现场传送，远端用户可远程观看拍摄现场的视频。 </p>
						<div class="col-md-6">
							<div style="margin-top: 60px;">
								<a href="<%=androidUrl %>" >
									<img src="welcom/images/android.png" />
								</a>
							</div>
							<div style="margin-top: 20px;">
								<a href="<%=pcUrl %>">
									<img src="welcom/images/pc.png" />
								</a>
							</div>
						</div>

						<div style="margin-top: 60px; float: right;">
							<!--<img src="images/download.png" title="扫描下载" />-->
							<div id="preview"></div>
							<h5 style="color: white;padding-left: 20px;">使用手机扫描下载</h5>
						</div>
					</div>
					<div class="col-md-6 banner-right">
						<div class="app-img">
							<img src="welcom/images/pic1.png" class="img-responsive" alt="" />
							<br />
							<img src="welcom/images/pic2.png" class="img-responsive" alt="" />
						</div>
					</div>
					<div class="clearfix"> </div>
				</div>
			</div>
		</div>
		<!--/header-->
		
		<!--services-->
		<div class="container" id="about">
			<div class="service-section-grids">
				<h3>业务介绍</h3>
				<div class="col-md-6 service-grid">
					<div class="service-section-grid">
						<div class="icon">
							<i class="s1"> </i>
						</div>
						<div class="icon-text">
							<h4>业务介绍</h4>
							<p>
								1.什么是即拍即传
								<br /> 即拍即传是一类现场直播服务产品，满足客户对视频内容录制并实时传播等需求，通过终端拍摄视频并接入网络现场传送，远端用户可远程观看拍摄现场的视频。
								<br /> 2.产品使用： 即拍即传产品适用于各类商业新闻媒体、企事业单位用户、婚庆或会务行业用户，可通过适配采集终端将现场视频快速传送到远端，供业务管理、产品展示等应用；也包括房产、家居、物流、餐饮等等各类需要视频传送行业。可用于企业、商业机构进行业务培训，招聘会等；交通、治安、工商等流动监管客户等，也包括有实时录制并快速传送视频的公众客户。
							</p>
						</div>
						<div class="clearfix"> </div>
					</div>
					<div class="service-section-grid">
						<div class="icon">
							<i class="s2"> </i>
						</div>
						<div class="icon-text">
							<h4>产品特点</h4>
							<p>
								1.云端服务
								<br /> 中心平台基于云架构，支持多并发直播需求；视频内容 (手机、专业摄像机、单兵等)可上传到云存储，管理应用方便
								<br /> 2.视频高清、高安全性
								<br /> 高清视频：视频内容清晰高效。即可实时拍摄回传1080P的全高清视频；账号密码登录机制以及QoS等策略来保障
								<br /> 3.安装部署快速
								<br /> 产品随时随地的搭建、快速部署，实现直播和远程的音视频观看和收听。方便用户即时启动应急即拍，有效处理事故 。
								<br /> 4.简单易用、高效共享
								<br /> 视频内容可通过互联网多用户共享；视频传送，投入简单、同步传输、时效性强，轻松获取第一手资料

							</p>
						</div>
						<div class="clearfix"> </div>
					</div>
				</div>
				<div class="col-md-6 service-grid">
					<div class="service-section-grid">
						<div class="icon">
							<i class="s3"> </i>
						</div>
						<div class="icon-text">
							<h4>终端类别</h4>
							<p>
								1.专业广播级终端，通过专业摄像机接入并拍摄传送，实现大型会议、婚礼等直播。
								<br /> 2.单兵采集终端，小型专业级一体化终端，提供给公安、交通、消防等客户执勤时随身携带，拍摄并传送到指挥中心，实现远程管理功能。
								<br /> 3.手机采集终端，手机安装APP录制并传送视频，手机采用硬件编码，编码效率高、耗电量小，可支持长时间不间断视频传送效果。
								<br /> 4.手机接入摄像机采集视频，手机和摄像机使用Wifi网络接入，视频流通过手机传送，可使用在航拍、车载等运动状态下视频直播。
							</p>
						</div>
						<div class="clearfix"> </div>
					</div>
					<div class="service-section-grid">
						<div class="icon">
							<i class="s4"> </i>
						</div>
						<div class="icon-text">
							<h4>功能特点</h4>
							<p>
								1.市场多元化
								<br /> 商业客户：商业新闻媒体、企事业单位用户、会务婚庆行业用户、交通、治安、工商等流动监管客户
								<br /> 个人客户:用于远程支持、社交、视频沟通等
								<br /> 2.专用平台
								<br /> 可满足视频传送需求。该产 品通过终端接入“即拍即传”视频专用平台
								<br /> 3.快速传送
								<br /> 视频传送，投入简单、同步传输、时效性强，轻松获取第一手资料
								<br /> 4.远程查看
								<br /> 可实现手机和电脑、PAD远程实时收看实时传送的视频， 及时高效。
								<br /> 5.多终端
								<br /> 产品支持手机、运动摄像机、专业直播终端、便携式单兵设备等均可实时采集视频实时传送。
							</p>
						</div>
						<div class="clearfix"> </div>
					</div>
				</div>
				<div class="clearfix"> </div>
			</div>
		</div>
		<!--/services-->
		<!--features-->
		<div class="features" id="features">
			<div class="container">
				<div class="features-head">
					<h3>功能介绍</h3>
				</div>
				<div class="features-grids">
					<div class="col-md-4 features-gird">
						<h4>01<i>.</i></h4>
						<h3>拍摄传送<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>支持音视频现场采集录制，视频经过编码压缩后通过电信4G网络实时传送视频到播放端。</p>
					</div>
					<div class="col-md-4 features-gird">
						<h4>02<i>.</i></h4>
						<h3>云存储<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>支持录制音视频信息远端传送到云平台。支持录制同步传送存储 。</p>
					</div>
					<div class="col-md-4 features-gird">
						<h4>03<i>.</i></h4>
						<h3>多点播放、分享<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>支持用户将现场视频、历史录像分享给其他用户，手机、PC、PAD观看现场实况直播。</p>
					</div>
					<div class="col-md-4 features-gird">
						<h4>04<i>.</i></h4>
						<h3>视频实时直播<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>支持浏览器如IE或专用客户端软件播放视频，视频具有良好的流畅度，有良好的网络适应能力，。支持智能手机、PC、PAD多终端直播。</p>
					</div>
					<div class="col-md-4 features-gird">
						<h4>05<i>.</i></h4>
						<h3>录像、回播、即播即录<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>支持用户录像、拍图，直播内容即播即录，长期有效，在本地或云端保存图片、录像内容，并支持客户端远程回放录像。</p>
					</div>
					<div class="col-md-4 features-gird">
						<h4>06<i>.</i></h4>
						<h3>GPS定位<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>支持GPS功能，根据用户需求，识别GPS信息并转化为识别位置信息字幕添加到视频内容中，用户浏览视频同时可看到录制视频的位置信息。</p>
					</div>
					<div class="col-md-4 features-gird">
						<h4>07<i>.</i></h4>
						<h3>语音传送<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>支持直播前端用户通过特定的按键录制音频信息并传送。</p>
					</div>
					<div class="col-md-4 features-gird">
						<h4>08<i>.</i></h4>
						<h3>预约收播<i>.</i></h3>
						<div class="clearfix"> </div>
						<p>可通过邮件、短信、微信等批量预约邀请收看直播内容。</p>
					</div>
					<div class="col-md-4 features-gird">
					</div>
					<div class="clearfix"> </div>
				</div>
			</div>
		</div>
		<!--/features-->
		<!--showcase-->
		<div class="about-section" id="user">
			<div class="container">
				<div class="features-head">
					<h3>成功案例</h3>
				</div>
				<div class="col-md-12 " style="margin-top: 20px;">
					<div class="col-md-6 about_left">
						<img src="welcom/images/user.png" alt="" />
					</div>
					<div class="col-md-6 about_right">
						<h3>1.电力施工及设备监控</h3>
						<p>某电力公司为了更好的管理下属的施工队伍，对施工队的现场施工情况能实时掌握， 需要新建一套视频系统来满足需求业务流程远程现场管理需求，主要实现施工前， 视频查看现场施工人员着装规范；施工过程管理，固定手机全程拍摄施工现场视频， 利用视频信息监测作业规范、设备状态、排除安全隐患； 视频信息管理，可对视频归档、备注等由后台进行统一管理。
						</p>
					</div>
				</div>

				<div class="col-md-12" style="margin-top: 10px;margin-bottom: 20px;">
					<div class="col-md-6 about_right">
						<h3>2.城管执法指挥</h3>
						<p>城管远程指挥系统：指挥人员可对执法过程、紧急情况等实施远程指挥调度、远程取证、以及对现场执行工作进行规范化管理， 执法人员配备无线传输单兵，将执行过程实时视频通过4G网络传送到城管执法指挥中心大屏，中心通过远程画面进行调度指挥， 同时实现将执法过程中的影像实时录存，建立执法数据库，规范执法过程，解决了城管执行任务时存在执法取证难、 上级指挥信息沟通难、记录执法过程难等问题。
						</p>
					</div>
					<div class="col-md-6 about_left">
						<img src="welcom/images/fayuan.jpg" alt="" />
					</div>
				</div>
				<div class="clearfix"> </div>
			</div>
		</div>
		<!--/shecase-->
		<!--Footer-->
		<!--<div class="footer" style="height: 200px;">
			<div class="container">
				<div class="footer-head">
					<h5>中国电信集团 版权所有</h5>
				</div>
			</div>
		</div>-->
		<div class="footer" id="footer">
			<div class="container">
				<div class="footer-head">
					<h3>©2015 视频服务产品（上海）运营中心 版权所有</h3>
				</div>
				<div class="footer-bottom">
					<div class="col-md-6 footer-about">
						<h4>联系我们<i>.</i></h4>
						<h6>视频服务产品（上海）运营中心</h6>
						<h5>联系方式</h5>
						<ul>
							<li><span class="call"> </span></li>
							<li>
								<p>400-920-8566</p>
							</li>
						</ul>
						<ul>
							<li><span class="msg"> </span></li>
							<li><a href="malito:spzx@sttri.com.cn">spzx@sttri.com.cn</a></li>
						</ul>
						<ul>
							<li><span class="locator"> </span>
								<li>
									<p>上海市浦东新区杨高南路5788号</p>
								</li>
						</ul>
					</div>
					<!--<div class="col-md-6 footer-news">
						<h4>latest news<i>.</i></h4>
						<div class="footer-news-info">
							<div class="footer-img">
								<img src="welcom/images/nwes-1.jpg" class="img-responsive" alt="" />
							</div>
							<div class="footer-right">
								<h5>just a super cool  person on fire<i>.</i></h5>
								<ul>
									<li>
										<i class="chat"> </i>
										<p><a href="#">24 Comments</a></p>
									</li>
									<li>
										<i class="date"> </i>
										<p>17 Jul 2014</p>
									</li>
								</ul>
							</div>
							<div class="clearfix"> </div>
						</div>
					</div>-->
					<div class="clearfix"> </div>
				</div>
			</div>
		</div>
		<!--/Footer-->
	</body>
	<script type="text/javascript">
	        
	        (new GoTop()).init({
				 pageWidth:980,
				 nodeId:'go-top',
				 nodeWidth:50,
				 distanceToBottom:125,
				 distanceToPage:220,
				 hideRegionHeight:150,
				 text:'Top'
			});
			
	</script>
</html>
