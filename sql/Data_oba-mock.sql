INSERT INTO ACCESS_MAPPING_CHANNEL (CHANNEL,FUNCTIONS,LOGIN_ACTIVE) VALUES
	 ('2','["investment", "travel_card", "payment_account_deduct", "payment_cash", "payment_atm"]',1),
	 ('3','["investment", "travel_card", "payment_account_deduct", "payment_cash", "payment_atm"]',1),
	 ('4','["investment", "travel_card", "payment_account_deduct", "payment_cash", "payment_atm"]',1),
	 ('8','["investment", "travel_card", "payment_account_deduct", "payment_cash", "payment_atm"]',1),
	 ('9','["investment", "payment_account_deduct", "payment_cash", "payment_atm"]',1),
	 ('oc','["investment", "payment_atm"]',1);

INSERT INTO ACCESS_MAPPING_TYPE (TYPE,FUNCTIONS,LOGIN_ACTIVE) VALUES
	 ('branch','["investment", "travel_card", "payment_account_deduct", "payment_cash", "payment_atm"]',1),
	 ('private','["investment", "payment_atm"]',1),
	 ('wealth','["investment", "payment_atm"]',1),
	 ('wealth_specialist','["payment_atm"]',1);

INSERT INTO APPROVAL_PERMISSION (APPL_BRANCH_ID,EMPLOYEE_ID,AUTH_LEVEL) VALUES
	 ('PRVT/','s99999','SC');

	
INSERT INTO BRANCH (BRANCH_ID,NAME_EN,NAME_TH,REGION_CODE,CHANNEL_TYPE,IS_BOOKING_BRANCH,OWN_BRANCH_ONLY) VALUES
	 ('0','s99999','เทส','0','2',0,0);

INSERT INTO EMPLOYEE (EMPLOYEE_ID,FIRSTNAME_EN,LASTNAME_EN,FIRSTNAME_TH,LASTNAME_TH,LICENSE_ID,OC_CODE,OC_NAME,OC_NAME_TH,EMAIL,POSITION_NAME,SAM_EMPLOYEE_ID,SAM_FIRSTNAME_EN,SAM_LASTNAME_EN,SAM_FIRSTNAME_TH,SAM_LASTNAME_TH,SAM_OC_CODE,SAM_OC_NAME,SAM_OC_NAME_TH,SAM_EMAIL) VALUES
	 ('s99999','test','test','เทส','เทส','0','0','test','เทส','s99999@scb.co.th','Maker','0','test','test','เทส','เทส','0','test','เทส','s99999@scb.co.th');

INSERT INTO GENERAL_PARAM (TYPE,VALUE,SERVER_ONLY) VALUES
	 ('ALLOW_FOREIGNER_ROLES','{"list": ["branch"]}','Y'),
	 ('ALLOW_LOGIN_BRANCH','{"list": null}','Y'),
	 ('BRANCH','{"list": [""]}','Y'),
	 ('CONSENT','{"list": ["crossSelling", "marketing", "privacyNotice", "sensitive"]}','N'),
	 ('FC_PING_AN_BRANCH','{"list": ["0001", "0002", "0003", "0004", "0005", "0006", "0007", "0008", "0010", "0011", "0012", "0013", "0014", "0015", "0016", "0017", "0018", "0019", "0020", "0021", "0022", "0023", "0024", "0026", "0027", "0028", "0030", "0031", "0032", "0033", "0034", "0036", "0037", "0038", "0039", "0040", "0041", "0042", "0043", "0045", "0046", "0047", "0048", "0049", "0050", "0051", "0052", "0053", "0054", "0055", "0056", "0057", "0058", "0059", "0060", "0061", "0065", "0066", "0067", "0068", "0071", "0072", "0075", "0076", "0077", "0078", "0080", "0081", "0085", "0086", "0088", "0089", "0090", "0092", "0093", "0094", "0095", "0096", "0099", "0101", "0102", "0104", "0105", "0106", "0108", "0109", "0111", "0112", "0114", "0115", "0117", "0118", "0119", "0120", "0121", "0122", "0123", "0127", "0130", "0131", "0132", "0133", "0136", "0137", "0139", "0140", "0142", "0143", "0145", "0146", "0147", "0148", "0149", "0151", "0152", "0154", "0155", "0156", "0160", "0161", "0162", "0164", "0165", "0166", "0167", "0168", "0169", "0170", "0171", "0173", "0174", "0175", "0177", "0181", "0184", "0185", "0187", "0189", "0191", "0192", "0193", "0194", "0201", "0202", "0203", "0204", "0206", "0208", "0210", "0211", "0212", "0215", "0216", "0217", "0218", "0219", "0221", "0223", "0224", "0227", "0231", "0232", "0233", "0234", "0235", "0236", "0237", "0239", "0240", "0242", "0244", "0245", "0246", "0247", "0248", "0249", "0250", "0253", "0254", "0257", "0258", "0260", "0263", "0264", "0268", "0269", "0270", "0271", "0272", "0273", "0274", "0275", "0278", "0279", "0281", "0284", "0289", "0290", "0291", "0292", "0295", "0296", "0298", "0301", "0302", "0304", "0305", "0308", "0309", "0310", "0311", "0312", "0313", "0314", "0315", "0316", "0317", "0318", "0319", "0320", "0321", "0322", "0323", "0324", "0325", "0326", "0327", "0328", "0329", "0330", "0331", "0333", "0334", "0335", "0337", "0338", "0339", "0340", "0341", "0342", "0343", "0344", "0346", "0347", "0348", "0349", "0350", "0351", "0352", "0353", "0354", "0355", "0356", "0357", "0360", "0361", "0364", "0365", "0366", "0368", "0370", "0371", "0372", "0374", "0376", "0380", "0381", "0382", "0383", "0385", "0386", "0387", "0388", "0390", "0391", "0392", "0393", "0395", "0397", "0399", "0515", "0519", "0570", "0649", "0713", "0764", "0769", "0780", "0830", "0838", "0882", "0883", "0928", "0929", "0962", "5017", "5024", "5030", "5031", "5037", "5038", "5048", "5051", "5052", "5053", "5055", "5056", "5057", "5059", "5061", "5062", "5063", "5066", "5067", "5068", "5070", "5072", "5073", "5075", "5076", "5079", "5080", "5084", "5086", "5087", "5088", "5089", "5090", "5092", "5109", "5111", "5112", "5116", "5117", "5120", "5126", "5128", "5129", "5130", "5136", "5139", "5144", "5147", "5149", "5151", "5152", "5156", "5159", "5160", "5162", "5170", "5173", "5176", "5180", "5184", "5189", "5190", "5193", "5198", "5200", "5202", "5206", "5210", "5214", "5221", "5228", "5229", "5235", "5239", "5240", "5250", "5258", "5280", "5286", "5300", "5305", "5310", "5325", "5331", "5340", "5341", "5344", "5345", "5350", "5352", "5358", "5360", "5361", "5367", "5403", "5409", "5412", "5415", "5418", "5419", "5420", "5450", "5430", "5460", "5444", "5400", "5443", "5470", "5439", "5451", "5452", "5457", "5453", "5455", "5458", "5481", "5468", "5472", "5490", "5480", "5714", "5715", "5704", "5702", "5707", "5742", "5712", "5730", "5703", "5736", "5701", "5716", "5852", "5853", "5854", "5851", "5865"]}','Y'),
	 ('FC_SCORE_TS','{"hi": 4600, "lo": 3600, "vendor": "UTC"}','Y'),
	 ('FC_SCORE_TS_PA','{"hi": 69, "lo": 65, "vendor": "PingAn"}','Y'),
	 ('FOREIGNER_ALLOW_PRODUCTS','{"list": ["ST02SE02"]}','Y'),
	 ('FRONTEND','{"manual_keyin": true, "service_only": true}','N'),
	 ('KYC','{"list": [307, 309, 315, 316, 317, 318, 319, 320, 321]}','Y');
INSERT INTO GENERAL_PARAM (TYPE,VALUE,SERVER_ONLY) VALUES
	 ('VERIFY_MULTI_LOGIN','{"isVerify": true}','N');

INSERT INTO LOGIN_BRANCH (BRANCH_ID,EMPLOYEE_ID,APPL_BRANCH_ID,ROLES) VALUES
	 ('0','s99999','PRVT/','["branch"]');

INSERT INTO LOGIN_SESSION (EMPLOYEE_ID,LAST_ACTIVITY_TIME,DEVICE_ID,STATUS,APP_NAME,TOKEN) VALUES
	 ('123213','2023-08-03 12:07:01','web','active','ONBD','10.248.2.140'),
	 ('213213','2023-08-03 11:32:19','web','active','ONBD','10.248.2.140'),
	 ('2312312','2023-08-03 11:50:34','web','active','ONBD','10.248.2.140'),
	 ('asdsadas','2023-08-03 11:30:25','web','active','ONBD','10.248.2.140'),
	 ('dsadasd','2023-08-03 11:39:22','web','active','ONBD','10.248.2.140'),
	 ('sadasd','2023-08-03 11:42:57','web','active','ONBD','10.248.2.140'),
	 ('s99999','2023-12-14 17:17:25','web','active','ONBD','192.168.1.103');

INSERT INTO ORGANIZATION (OC_CODE,OC_NAME_TH,OC_NAME_EN) VALUES
	 ('0','เทส','s99999');
	

INSERT INTO GENERAL_PARAM (TYPE, VALUE, SERVER_ONLY) VALUES ('FACE_NOT_ALLOW_BRANCH', '{"list": [""]}', 'Y');
INSERT INTO GENERAL_PARAM (TYPE, VALUE, SERVER_ONLY) VALUES ('FACE_VENDER', '{ "vender":"PingAn"}', 'Y');
INSERT INTO GENERAL_PARAM (TYPE, VALUE, SERVER_ONLY) VALUES ('FACE_NOT_ALLOW_ROLE', '{"list": ["bulk","dsa"]}', 'Y');