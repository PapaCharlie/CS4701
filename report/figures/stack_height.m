heights = 5:15;
means_d1 = [49.9,66.4,68.25,87.7,88.9,65.75,91.15,58.7,72.35,59.85,57.4];
means_d2 = [113.45,127.65,138.9,157.7,123.1,195.4,119.2,131.1,161.1,120.65,128.3];
means_d3 = [161.35,158.25,310.2,244.45,408.8,352.0,356.75,261.05,295.3,245.95,185.65];
means_d4 = [581.0,796.2,753.6,955.2,1295.7,1221.1,1178.5,800.4,781.9,527.1,786.3];
means_d5 = [435.3,903.2,884.4,1657.6,1731.9,1685.3,1288.7, 1581.9,2050.3,1002.2,567.3];

fig = figure;
hold on;
plot(heights, means_d1, '+-', 'LineWidth', 2);
plot(heights, means_d2, '+-', 'LineWidth', 2);
plot(heights, means_d3, '+-', 'LineWidth', 2);
plot(heights, means_d4, '+-', 'LineWidth', 2);
plot(heights, means_d5, '+-', 'LineWidth', 2);
hold off;
xlabel 'Maximum Stack Height'
ylabel 'Time Alive (Mean of 20 simulations)'
legend('Search Depth = 1', 'Search Depth = 2', 'Search Depth = 3', 'Search Depth = 4', 'Search Depth = 5', 'Location', 'northwest')
saveas(fig, 'height_v_time_dall.pdf');
% close all

depths = 1:5;
mean_times = [48.1,192.4,243.9,1304.7,1706.1];
fig = figure;
plot(depths, mean_times, 'r+-', 'LineWidth', 2);
xlabel 'Lookahead Length'
ylabel 'Time Alive (Mean of 20 simulations)'
saveas(fig, 'lookahead_v_time.pdf');
close all
