package quiz;

import java.util.*;
import java.io.*;


abstract class QuizObject {
	protected int score;
	protected int numQuestions; //the number of questions in the quiz
	protected List <Integer> correctAnswers; //record keeping, index represents question num, incorrect = -1, not answered = 0, correct = 1; defaults to 0 
	protected List<String> quizTextLines;//the loaded quiz, each element is a line
	protected List<Integer> quesBeginIndexes;//taking note of where each question Q line  begins
	protected List<Integer> answeredQuestions;//record of which questions were answered
	protected List<String> answersGiven;//storing the actual answers for printing
	protected String startDate = null;//start of quiz
	protected String endDate = null;//end of quiz

	
	public void loadFile(String fileName) {
		BufferedReader reader = null;
		this.numQuestions = 0;
		this.quesBeginIndexes = new ArrayList<Integer>();
		this.quizTextLines = new ArrayList<String>();
		this.correctAnswers = new ArrayList<Integer>();
		answersGiven = new ArrayList<String>();
		answeredQuestions = new ArrayList<Integer>();
		
		try {
			reader = new BufferedReader(new FileReader(fileName));
			
			String line = reader.readLine();
			while (line != null) {
				quizTextLines.add(line);
				line = reader.readLine();
			}

			
			for (int i = 0; i < quizTextLines.size(); i++) {
				if (quizTextLines.get(i).startsWith("QT")) {
					this.numQuestions++;
					this.quesBeginIndexes.add(i+1);
					this.correctAnswers.add(0);
					this.answersGiven.add("");
					
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void startQuiz() {
		int correct; // -1 incorrect, 1 correct, 0 no answer
		startDate = GetDate.getDate();
		while(true) {
			System.out.println("Select a question or enter q to end quiz");
		
			for (int i = 0; i < numQuestions; i++) {
				if (answeredQuestions.contains(i)) //if question was already attempted
					System.out.println(i + quizTextLines.get(quesBeginIndexes.get(i)).substring(1) + " (answered: " + answersGiven.get(i) + ")"); //e.g. (answered: 0 2 3)
				else // question not yet attempted
					System.out.println(i + quizTextLines.get(quesBeginIndexes.get(i)).substring(1));
			}
			Scanner userInput = new Scanner(System.in);
			String stringUserChoice = userInput.next();

			
			if (stringUserChoice.equals("q")) {
				endDate = GetDate.getDate();
				endTest();
				return;
			}
			int userChoice;
			try {
				userChoice = Integer.parseInt(stringUserChoice);
			}
			catch (Exception e) {
				System.out.println("Choose between 0 and " + (numQuestions-1));
				continue;
			}
			
			if (userChoice > numQuestions-1 || userChoice < 0) {
				System.out.println("Choose between 0 and " + (numQuestions-1));
				continue;
			}			

			int i = quesBeginIndexes.get(userChoice);
			int j = quesBeginIndexes.get(userChoice)+1;
			StringBuilder answerGiven = new StringBuilder(); //passed by reference to record answer given
			
			if (quizTextLines.get(quesBeginIndexes.get(userChoice)-1).startsWith("QT TruthQuestion")) {
				while (!quizTextLines.get(j).startsWith("CA ")) {
					j++;
				}
				TruthQuestion truthQues = new TruthQuestion(quizTextLines.subList(i, j+1), answerGiven);//create question instance with relevant subList of quiz text passed
				try {
					correct = truthQues.promptUser();
					recordAnswer(userChoice, correct, answerGiven);
				}
				catch (InvalidInput e) {
					continue;
				}

			}
			else if (quizTextLines.get(quesBeginIndexes.get(userChoice)-1).startsWith("QT SingleAnswerQuestion")) {

				while (!quizTextLines.get(j).startsWith("CA ")) {
					j++;
				}
				SingleAnswerQuestion singleAnswerQues = new SingleAnswerQuestion(quizTextLines.subList(i, j+1), answerGiven);
				try {
					correct = singleAnswerQues.promptUser();
					recordAnswer(userChoice, correct, answerGiven);
				}
				catch (InvalidInput e) {
					continue;
				}


			}
			else if (quizTextLines.get(quesBeginIndexes.get(userChoice)-1).startsWith("QT MultiAnswerQuestion")) {

				while (!quizTextLines.get(j).startsWith("CA ")) {
					j++;
				}
				MultiAnswerQuestion multiAnswerQues = new MultiAnswerQuestion(quizTextLines.subList(i, j+1), answerGiven);
				try {
					correct = multiAnswerQues.promptUser();
					recordAnswer(userChoice, correct, answerGiven);
				}
				catch (InvalidInput e) {
					continue;
				}


					
			}
			
		}
	}
	
		
	private void recordAnswer(int questionNum, int correct, StringBuilder answerGiven) {
		answersGiven.set(questionNum, answerGiven.toString());
		// answerGiven, which is of type StringBuilder, was passed by reference to the QuestionObject instance to record the given answer
		// this is because two pieces of information are required from the class, whether the answer was correct and the answer itself
		// the given answer is now recorded in the answersGiven list.
		correctAnswers.set(questionNum, correct);
		if (!answeredQuestions.contains(questionNum))
			answeredQuestions.add(questionNum);
	}


	abstract void endTest();
		 
	
}
