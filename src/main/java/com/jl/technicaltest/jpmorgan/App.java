package com.jl.technicaltest.jpmorgan;

import com.jl.technicaltest.jpmorgan.model.Instruction;
import com.jl.technicaltest.jpmorgan.model.Report;

public interface App {
	public void initialize();

	public void processInstruction(Instruction instruction);

	public void showReport();

	public Report getReport();

}
