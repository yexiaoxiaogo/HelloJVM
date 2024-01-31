

# classfile

```shell
magic: cafe babe 

minor version: 0000 
major version: 0034 

[0] cpsize: 0018 (24 包含自己)
[1] method ref: 0a   = 10
    class index: 00 03      =3
    nameAndTypeIndex: 00 15     =21
[2] class: 07
    nameIndex: 0016     =22
[3] class: 07
    nameIndex: 00 17      =23
[4] utf8: 01 
    length: 0006        = 6
    bytes: 3c69 6e69 743e     =<init>
[5] utf8: 01
    length: 00 03       =3
    bytes: 28 2956      =()V
[6] utf8: 01
    length: 00 04
    bytes: 43 6f64 65   =Code
[7] utf8: 01 
    length: 000f
    bytes: 4c69 6e65 4e75 6d62 6572 5461 626c 65    =LineNumberTable
[8] utf8: 01 
    length: 0012
    bytes: 4c6f 6361 6c56 6172 6961 626c 6554 6162 6c65 =LocalVariableTable
[9] utf8: 01
    length: 00 04
    bytes: 74 6869 73   =this
[10] utf8: 01
    length: 0008
    bytes: 4c48 656c 6c6f 323b  =LHello2;
[11] utf8: 01
    length: 00 04
    bytes: 6d 6169 6e       =main
[12] utf8: 01 
    length: 0016 
    bytes: 285b 4c6a 6176 612f 6c61 6e67 2f53 7472 696e 673b 2956   =([Ljava/lang/String;)V
[13] utf8: 01
    length: 00 04
    bytes: 61 7267 73           =args
[14] utf8: 01 
    length: 0013    =19
    bytes: 5b4c 6a61 7661 2f6c 616e 672f 5374 7269 6e67 3b      =[Ljava/lang/String;
[15] utf8:01 
    length: 0001 
    bytes: 61       =a
[16] utf8: 01 
    length: 0001 
    bytes: 49      =I
[17]utf8: 01 
    length: 0001 
    bytes: 62       =b
[18] utf8: 01 
    length: 0001 
    bytes: 63       =c
[19] utf8: 01 
    lenght: 000a 
    bytes: 536f 7572 6365 4669 6c65         =SourceFile
[20] utf8: 01
    length : 00 0b
    bytes: 48 656c 6c6f 322e 6a61 7661      =Hello2.java
[21] nameAndType: 0c        =12
    nameIndex: 00 04           =4
    descriptionIndex: 00 05         =5
[22] utf8: 01 
    length: 0006 
    bytes: 4865 6c6c 6f32               =Hello2
[23] utf8: 01
    length: 00 10
    bytes: 6a 6176 612f 6c61 6e67 2f4f 626a 6563 74     =java/lang/Object


accessFlag:  00 21
thisClass: 00 02
superClass: 00 03     => nameIndex 23  => java/lang/Object
interfaceCount: 00 00
filedCount: 00 00


methodCount: 00 02
[0]method:
    accessFlag: 00 01
    nameIndex: 00 04   ==<init>
    descIndex: 00 05    =()V
    attrCount: 00 01
    attributes:
        [0]attr: 
            attrNameIndex: 00 06  （常量池第 6 个） =Code
            attrLength: 00 0000 2f  ==47
                Code:
                    maxStack: 00 01
                    maxLocals: 00 01
                    codeLength: 00 0000 05
                    byteCode: 2a b700 01b1    =转指令 
                        - 2a = aload0 
                        - b70001 = empty
                        - b1 = return
                    exceptionTalbeLength: 0000 
                    codeAttributeCount: 0002 
                        - [0]attrNameIndex: 0007 ===LineNumberTable
                            attrLength: 0000 0006
                            length: 0001 
                                [0]Line: 
                                    startPc: 0000 
                                    lineNumber: 0003 
                        - [1]attrNameIndex: 0008 ==LocalVariableTable
                            attrLength: 0000 000c 
                            attrContent: 0001 0000 0005 0009 000a 0000 
[1]method: 
    accessflag: 0009
    nameIndex: 000b     =main
    descIndex: 000c     =([Ljava/lang/String;)V
    attrcount: 0001 
    attributes:
        [0] attr:
            attrNameIndex: 0006        =Code
            attrLength: 0000 005f       =95
                Code: 
                    maxStack: 0002 
                    maxLocals: 0004 
                    codeLength: 0000 000b
                    byteCode: 1047 3c10 523d 1b1c 603e b1   ===转指令
                        -10 bipush 0x47
                        -3c istore1 保持值 slots
                        -10 bipush 0x52
                    exceptionTableLength: 00 00
                    codeAttributeCount: 00 02
                        - [0] attrNameIndex: 00 07  ==LineNumberTalbe
                            attrLength: 00 0000 12  =18
                            length: 00 04
                                [0]line: 
                                    startPc: 00 00
                                    lineNumber: 00 09
                                [1]line
                                    startPc: 00 03
                                    lineNumber: 00 0a
                                [2]line:
                                    startPc: 00 06
                                    lineNuber: 00 0b
                                [3]line: 
                                    startpc: 00 0a
                                    lineNumber: 00 0c
                        - [1] attrNameIndex: 00 08   =LocalVariableTable
                            attrLength: 00 0000 2a  =42
                            attrContent: 00 0400 0000 0b00 0d00 0e00 0000 0300 0800 0f00 
                                        1000 0100 0600 0500 1100 1000 0200 0a00 0100 1200 1000 03


attributeCount: 00 01
    attrNameIndex: 00 13    =SourceFile
    attrbiuteLength: 00 0000 02
    sourceFileIndex: 00 14      =Hello2.java
```