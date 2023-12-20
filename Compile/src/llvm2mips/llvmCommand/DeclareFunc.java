package llvm2mips.llvmCommand;

import Type.Type;
import llvm2mips.Mips;
import llvm2mips.mipsLine.mipsTextLine.LineExegesis;
import llvm2mips.mipsLine.mipsTextLine.LineLabel;
import llvm2mips.mipsLine.mipsTextLine.LineLw;
import llvm2mips.mipsLine.mipsTextLine.LineSw;

import java.util.List;
import java.util.Map;

public class DeclareFunc extends LlvmCom {
    boolean is_dso_local;
    Type type;
    String name;
    List<FFParam> ffParams;

    public DeclareFunc(boolean is_dso_local, Type type, String name, List<FFParam> ffParams) {
        this.is_dso_local = is_dso_local;
        this.type = type;
        this.name = name;
        this.ffParams = ffParams;
    }

    @Override
    public String toString() {
        // # define dso_local void @f2(i32 %a0)
        StringBuilder sb = new StringBuilder("define ");
        if (is_dso_local) {
            sb.append("dso_local ");
        }
        sb.append(this.type.toString())
                .append(" @")
                .append(this.name)
                .append("(");
        if (this.ffParams != null) {
            if (this.ffParams.size() > 0) {
                sb.append(this.ffParams.get(0).toString());
            }
            sb.append(")");
            for (FFParam ffParam : this.ffParams) {
                sb.append(", ")
                        .append(ffParam.toString());
            }
        }
        return "";
    }

    @Override
    public void analyzeCom() {
        // 注释
        Mips.getMips().addTextLines(new LineExegesis(this.toString()));
        if (!this.is_dso_local) {
            // 放入口define
            return;
        }
        // 打印label行
        Mips.getMips().addTextLines(new LineLabel(this.name));
        // 加载参数
        if (this.ffParams == null) {
            return;
        }
        int top = 4; // 指向最后一个参数的转存位置
        for (int i = this.ffParams.size() - 1; i >= 0; i--) {
            // lw rt, ty1($sp)
            // sw rt, ty2($sp)
            try {
                String rt = Mips.getMips().getRegister();
                Mips.getMips().addTextLines(new LineLw(rt, top));
                int len = super.getTy(ffParams.get(i).getType(), ffParams.get(i).typeHasP);
                top += 4 * len;
                int ty = Mips.getMips().allocLoc(len);
                Mips.getMips().addTextLines(new LineSw(rt, ty));
                Mips.getMips().freeRegister(rt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
