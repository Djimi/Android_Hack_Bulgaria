

import java.awt.Color;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

public class stainedGlassFilter {

	
	public static void main(String[] args)  {
		
		if(args.length < 3){
			throw new IllegalArgumentException("You have to enter at least 3 arguments !");
		}
		
		if(Integer.parseInt(args[2]) < 0){
			throw new IllegalArgumentException("Enter a non-negative number !");
		}
		
		final String path = args[0];
		final String newPath = args[1];
		
		
		long start = System.currentTimeMillis();
		
		final PictureForChange picture = new PictureForChange(path, 10000 );
		
		try {
			picture.generatePoints();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		final int threadCount = Runtime.getRuntime().availableProcessors();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
	
		
		threadPoolSetup(picture, threadPool, threadCount, picture.getImage().getWidth());

		threadPool.shutdown();
		
		
		while(!threadPool.isTerminated()){
		};
		
		picture.setAverageColorsForRegions();

		picture.paintRegionsToImage();
		
		// Writing the new image
		File output = new File(newPath);
		
		try {
			ImageIO.write(picture.getImage(), "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println((end - start)/1000);
	
	}
	
		
	
	public static void threadPoolSetup(PictureForChange picture, ExecutorService threadPool, int threadCount, int width){
		
		int startRow = 0;
		int endRow = picture.getImage().getHeight();
		final int rowsInPart = endRow/threadCount;
		int to = rowsInPart;
		
		
		for(int i = 0; i < threadCount - 1; ++i){
			
			threadPool.submit(new CoreStarter(startRow, to, width, picture) );
			
			startRow += rowsInPart;
			to += rowsInPart;
		}
		
		threadPool.submit(new CoreStarter(startRow, endRow, width, picture));
		
	}
}



//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Collections;
//
//public class Details {
//
//   public static void main(String a[]){
//       List<String> syncal = 
//         Collections.synchronizedList(new ArrayList<String>());
//
//       //Adding elements to synchronized ArrayList
//       syncal.add("Pen");
//       syncal.add("NoteBook");
//       syncal.add("Ink");
//
//       System.out.println("Iterating synchronized ArrayList:");
//       synchronized(syncal) {
//       Iterator<String> iterator = syncal.iterator(); 
//       while (iterator.hasNext())
//          System.out.println(iterator.next());
//       }
//   }
//}







//import java.awt.Color;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;
//
//import edu.wlu.cs.levy.CG.KDTree;
//import edu.wlu.cs.levy.CG.KeyDuplicateException;
//import edu.wlu.cs.levy.CG.KeySizeException;
//
//public class Main {
//
//	public static void main(String[] args) throws IOException, KeySizeException, KeyDuplicateException{
//		long start = System.currentTimeMillis();
//		
//		PictureForChange picture = new PictureForChange("input.jpg", 10000);
//		
//		picture.generatePoints();
//		
//		
//		
//		Color color = null;
//		Region currentRegion = null;
//		
//		for(int i = 0; i < picture.getImage().getHeight(); ++i){
//			for(int j = 0; j < picture.getImage().getWidth(); ++j){
//
//				color = new Color(picture.getImage().getRGB(j, i));
//				
//				int regionNumber = picture.getTree().nearest(new double[]{j, i});
//				
//				currentRegion = picture.getRegion(regionNumber);
//				
//				currentRegion.incrementColors(color.getRed(), color.getGreen(), color.getBlue());
//				currentRegion.incrementPoints();
//				currentRegion.getPoints().add(new double[]{j, i});
//				
//				
//			}
//		}
//
//		
//		
//		picture.setAverageColorsForRegions();
//
//		picture.paintRegionsToImage();
//		
//		File output = new File("savedNewSlower.jpg");
//		ImageIO.write(picture.getImage(), "jpg", output);
//		
//		long end = System.currentTimeMillis();
//		
//		System.out.println((end - start)/1000);
//	}
//	
//	
//}
