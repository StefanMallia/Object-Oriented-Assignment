import quiz.*;

public class Launcher {
	public static void main(String[] args) {
	
		StraightForwardGrading quiz;
		NegativeMarkingScheme negQuiz;
		
		if (args[0].equals("StraightForwardGrading")) {
			quiz = new StraightForwardGrading();
			quiz.loadFile(args[1]);
			quiz.startQuiz();
		}
		else if (args[0].equals("NegativeMarkingScheme")) {
			negQuiz = new NegativeMarkingScheme();
			negQuiz.loadFile(args[1]);
			negQuiz.startQuiz();
		}


	}
}
